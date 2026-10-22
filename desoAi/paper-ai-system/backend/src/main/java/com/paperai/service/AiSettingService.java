package com.paperai.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.paperai.dto.AiSettingRequest;
import com.paperai.entity.AiSetting;
import com.paperai.mapper.AiSettingMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户 AI 模型配置服务
 */
@Service
public class AiSettingService {

    /** 默认大模型配置（DeepSeek，OpenAI 兼容接口） */
    public static final String DEFAULT_BASE_URL = "https://api.deepseek.com/v1";
    public static final String DEFAULT_MODEL = "deepseek-chat";

    @Resource
    private AiSettingMapper aiSettingMapper;

    /**
     * 获取用户配置；未配置时返回默认值（API Key 为空，即使用 ai-service 全局配置）
     */
    public AiSetting getSetting(Long userId) {
        AiSetting setting = aiSettingMapper.selectOne(
                new LambdaQueryWrapper<AiSetting>().eq(AiSetting::getUserId, userId));
        if (setting == null) {
            setting = new AiSetting();
            setting.setUserId(userId);
            setting.setApiKey("");
            setting.setBaseUrl(DEFAULT_BASE_URL);
            setting.setModel(DEFAULT_MODEL);
        }
        return setting;
    }

    /**
     * 保存（新增或更新）用户配置
     */
    public AiSetting saveSetting(Long userId, AiSettingRequest req) {
        AiSetting setting = aiSettingMapper.selectOne(
                new LambdaQueryWrapper<AiSetting>().eq(AiSetting::getUserId, userId));
        if (setting == null) {
            setting = new AiSetting();
            setting.setUserId(userId);
        }
        setting.setApiKey(req.getApiKey() == null ? "" : req.getApiKey().trim());
        setting.setBaseUrl(req.getBaseUrl() == null || req.getBaseUrl().trim().isEmpty()
                ? DEFAULT_BASE_URL : req.getBaseUrl().trim());
        setting.setModel(req.getModel() == null || req.getModel().trim().isEmpty()
                ? DEFAULT_MODEL : req.getModel().trim());
        if (setting.getId() == null) {
            aiSettingMapper.insert(setting);
        } else {
            aiSettingMapper.updateById(setting);
        }
        return setting;
    }
}
