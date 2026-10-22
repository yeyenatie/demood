package com.paperai.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.paperai.common.BizException;
import com.paperai.dto.AiModelRequest;
import com.paperai.entity.AiModel;
import com.paperai.entity.AiSetting;
import com.paperai.mapper.AiModelMapper;
import com.paperai.mapper.AiSettingMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户自配 AI 模型服务：支持每个用户添加多个模型，生成时选择使用哪个
 */
@Service
public class AiModelService {

    public static final String DEFAULT_BASE_URL = "https://api.deepseek.com/v1";
    public static final String DEFAULT_MODEL = "deepseek-chat";

    @Resource
    private AiModelMapper aiModelMapper;

    @Resource
    private AiSettingMapper aiSettingMapper;

    /**
     * 列出用户的自配模型；若用户没有模型但老版 ai_setting 里保存过配置，一次性迁移为模型
     */
    public List<AiModel> listByUser(Long userId) {
        List<AiModel> list = queryByUser(userId);
        if (list.isEmpty()) {
            AiSetting old = aiSettingMapper.selectOne(
                    new LambdaQueryWrapper<AiSetting>().eq(AiSetting::getUserId, userId));
            if (old != null && old.getApiKey() != null && !old.getApiKey().trim().isEmpty()) {
                AiModel m = new AiModel();
                m.setUserId(userId);
                m.setName(old.getModel() == null || old.getModel().trim().isEmpty()
                        ? "我的模型" : old.getModel().trim());
                m.setApiKey(old.getApiKey().trim());
                m.setBaseUrl(old.getBaseUrl() == null || old.getBaseUrl().trim().isEmpty()
                        ? DEFAULT_BASE_URL : old.getBaseUrl().trim());
                m.setModelName(old.getModel() == null || old.getModel().trim().isEmpty()
                        ? DEFAULT_MODEL : old.getModel().trim());
                aiModelMapper.insert(m);
                aiSettingMapper.deleteById(old.getId());
                list = queryByUser(userId);
            }
        }
        return list;
    }

    /**
     * 按 ID + 用户查询（防止越权访问他人模型）
     */
    public AiModel getByIdAndUser(Long userId, Long id) {
        return aiModelMapper.selectOne(new LambdaQueryWrapper<AiModel>()
                .eq(AiModel::getId, id)
                .eq(AiModel::getUserId, userId));
    }

    public AiModel create(Long userId, AiModelRequest req) {
        AiModel model = new AiModel();
        model.setUserId(userId);
        apply(model, req);
        aiModelMapper.insert(model);
        return model;
    }

    public AiModel update(Long userId, Long id, AiModelRequest req) {
        AiModel model = getByIdAndUser(userId, id);
        if (model == null) {
            throw new BizException(404, "模型不存在");
        }
        apply(model, req);
        aiModelMapper.updateById(model);
        return model;
    }

    public void delete(Long userId, Long id) {
        AiModel model = getByIdAndUser(userId, id);
        if (model == null) {
            throw new BizException(404, "模型不存在");
        }
        aiModelMapper.deleteById(id);
    }

    private List<AiModel> queryByUser(Long userId) {
        return aiModelMapper.selectList(new LambdaQueryWrapper<AiModel>()
                .eq(AiModel::getUserId, userId)
                .orderByDesc(AiModel::getId));
    }

    private void apply(AiModel model, AiModelRequest req) {
        if (req.getName() == null || req.getName().trim().isEmpty()) {
            throw new BizException(400, "请填写模型显示名");
        }
        if (req.getApiKey() == null || req.getApiKey().trim().isEmpty()) {
            throw new BizException(400, "请填写 API Key");
        }
        model.setName(req.getName().trim());
        model.setApiKey(req.getApiKey().trim());
        model.setBaseUrl(req.getBaseUrl() == null || req.getBaseUrl().trim().isEmpty()
                ? DEFAULT_BASE_URL : req.getBaseUrl().trim());
        model.setModelName(req.getModelName() == null || req.getModelName().trim().isEmpty()
                ? DEFAULT_MODEL : req.getModelName().trim());
    }
}
