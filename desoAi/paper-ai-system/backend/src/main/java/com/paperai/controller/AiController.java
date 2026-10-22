package com.paperai.controller;

import cn.hutool.json.JSONUtil;
import com.paperai.common.Result;
import com.paperai.dto.AiGenerateRequest;
import com.paperai.dto.AiModelRequest;
import com.paperai.dto.AiSettingRequest;
import com.paperai.dto.PaperSaveRequest;
import com.paperai.entity.AiModel;
import com.paperai.entity.AiSetting;
import com.paperai.service.AiModelService;
import com.paperai.service.AiQuotaService;
import com.paperai.service.AiService;
import com.paperai.service.AiSettingService;
import com.paperai.service.PaperService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AI 接口：题目推荐 / 大纲生成 / 章节生成 / 自配模型管理 / 连接测试 / 周次数配额
 * 配额规则：生成时选择使用自己的模型则不限次；选择默认模型则每周限 10 次（成功才扣减）
 */
@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Resource
    private AiService aiService;

    @Resource
    private PaperService paperService;

    @Resource
    private AiSettingService aiSettingService;

    @Resource
    private AiQuotaService aiQuotaService;

    @Resource
    private AiModelService aiModelService;

    /**
     * 题目推荐：modelId 为空用默认模型，否则用用户自配模型
     */
    @PostMapping("/topics")
    public Result<List<Map<String, Object>>> topics(@RequestBody AiGenerateRequest req,
                                                    HttpServletRequest request) {
        Long uid = userId(request);
        ResolvedModel rm = resolveModel(uid, req.getModelId());
        if (!rm.ownModel) {
            aiQuotaService.checkQuota(uid);
        }
        List<Map<String, Object>> result = aiService.generateTopics(
                req.getResearchDirection(), req.getPaperType(),
                rm.apiKey, rm.baseUrl, rm.model);
        if (!rm.ownModel) {
            aiQuotaService.consume(uid);
        }
        return Result.ok(result);
    }

    /**
     * 大纲生成；若携带 paperId，自动将大纲保存到该论文
     */
    @PostMapping("/outline")
    public Result<Map<String, Object>> outline(@RequestBody AiGenerateRequest req,
                                               HttpServletRequest request) {
        Long uid = userId(request);
        ResolvedModel rm = resolveModel(uid, req.getModelId());
        if (!rm.ownModel) {
            aiQuotaService.checkQuota(uid);
        }
        Map<String, Object> outline = aiService.generateOutline(
                req.getTitle(), req.getKeywords(), req.getPaperType(),
                rm.apiKey, rm.baseUrl, rm.model);
        if (req.getPaperId() != null) {
            PaperSaveRequest save = new PaperSaveRequest();
            save.setTitle(req.getTitle());
            save.setKeywords(req.getKeywords());
            save.setPaperType(req.getPaperType());
            save.setOutline(JSONUtil.toJsonStr(outline));
            save.setStatus("draft");
            paperService.update(uid, req.getPaperId(), save);
        }
        if (!rm.ownModel) {
            aiQuotaService.consume(uid);
        }
        return Result.ok(outline);
    }

    /**
     * 章节内容生成
     */
    @PostMapping("/section")
    public Result<Map<String, Object>> section(@RequestBody AiGenerateRequest req,
                                               HttpServletRequest request) {
        Long uid = userId(request);
        ResolvedModel rm = resolveModel(uid, req.getModelId());
        if (!rm.ownModel) {
            aiQuotaService.checkQuota(uid);
        }
        String content = aiService.generateSection(
                req.getTitle(), req.getSectionName(), req.getKeywords(),
                req.getPoints(), req.getContext(),
                rm.apiKey, rm.baseUrl, rm.model);
        if (!rm.ownModel) {
            aiQuotaService.consume(uid);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("content", content);
        return Result.ok(data);
    }

    // ==================== 自配模型管理（多模型） ====================

    /**
     * 获取当前用户的全部自配模型
     */
    @GetMapping("/models")
    public Result<List<AiModel>> models(HttpServletRequest request) {
        return Result.ok(aiModelService.listByUser(userId(request)));
    }

    /**
     * 新增一个自配模型
     */
    @PostMapping("/models")
    public Result<AiModel> createModel(@RequestBody AiModelRequest req,
                                       HttpServletRequest request) {
        return Result.ok(aiModelService.create(userId(request), req));
    }

    /**
     * 修改一个自配模型
     */
    @PutMapping("/models/{id}")
    public Result<AiModel> updateModel(@PathVariable Long id,
                                       @RequestBody AiModelRequest req,
                                       HttpServletRequest request) {
        return Result.ok(aiModelService.update(userId(request), id, req));
    }

    /**
     * 删除一个自配模型
     */
    @DeleteMapping("/models/{id}")
    public Result<Void> deleteModel(@PathVariable Long id,
                                    HttpServletRequest request) {
        aiModelService.delete(userId(request), id);
        return Result.ok(null);
    }

    // ==================== 旧版单配置（兼容保留） ====================

    /**
     * 获取当前用户的大模型配置
     */
    @GetMapping("/setting")
    public Result<Map<String, Object>> getSetting(HttpServletRequest request) {
        AiSetting s = aiSettingService.getSetting(userId(request));
        Map<String, Object> data = new HashMap<>();
        data.put("apiKey", s.getApiKey());
        data.put("baseUrl", s.getBaseUrl());
        data.put("model", s.getModel());
        return Result.ok(data);
    }

    /**
     * 保存当前用户的大模型配置
     */
    @PutMapping("/setting")
    public Result<Map<String, Object>> saveSetting(@RequestBody AiSettingRequest req,
                                                   HttpServletRequest request) {
        AiSetting s = aiSettingService.saveSetting(userId(request), req);
        Map<String, Object> data = new HashMap<>();
        data.put("apiKey", s.getApiKey());
        data.put("baseUrl", s.getBaseUrl());
        data.put("model", s.getModel());
        return Result.ok(data);
    }

    /**
     * 测试大模型配置是否可用
     */
    @PostMapping("/test")
    public Result<Map<String, Object>> test(@RequestBody AiSettingRequest req) {
        return Result.ok(aiService.testConnection(req.getApiKey(), req.getBaseUrl(), req.getModel()));
    }

    // ==================== 配额 ====================

    /**
     * 查询当前用户本周 AI 生成配额使用情况（默认模型维度）
     */
    @GetMapping("/quota")
    public Result<Map<String, Object>> quota(HttpServletRequest request) {
        Long uid = userId(request);
        Map<String, Object> data = new HashMap<>();
        data.put("used", aiQuotaService.getUsed(uid));
        data.put("limit", aiQuotaService.getLimit());
        data.put("remaining", aiQuotaService.getRemaining(uid));
        return Result.ok(data);
    }

    // ==================== 内部工具 ====================

    /**
     * 解析本次生成使用的模型：modelId 指向用户自配模型则用自配（不限次）；
     * modelId 为空或模型不存在则回退默认模型（受周次数限制）
     */
    private ResolvedModel resolveModel(Long uid, Long modelId) {
        if (modelId != null) {
            AiModel m = aiModelService.getByIdAndUser(uid, modelId);
            if (m != null && m.getApiKey() != null && !m.getApiKey().trim().isEmpty()) {
                return new ResolvedModel(m.getApiKey(), m.getBaseUrl(), m.getModelName(), true);
            }
        }
        return new ResolvedModel(null, null, null, false);
    }

    private Long userId(HttpServletRequest request) {
        return (Long) request.getAttribute("userId");
    }

    /** 解析后的模型配置 */
    private static class ResolvedModel {
        final String apiKey;
        final String baseUrl;
        final String model;
        final boolean ownModel;

        ResolvedModel(String apiKey, String baseUrl, String model, boolean ownModel) {
            this.apiKey = apiKey;
            this.baseUrl = baseUrl;
            this.model = model;
            this.ownModel = ownModel;
        }
    }
}
