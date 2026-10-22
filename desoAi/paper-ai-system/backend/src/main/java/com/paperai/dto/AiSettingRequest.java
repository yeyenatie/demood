package com.paperai.dto;

import lombok.Data;

/**
 * 用户 AI 模型配置请求
 */
@Data
public class AiSettingRequest {

    private String apiKey;

    private String baseUrl;

    private String model;
}
