package com.paperai.dto;

import lombok.Data;

/**
 * 用户自配 AI 模型新增 / 修改请求
 */
@Data
public class AiModelRequest {

    private Long id;

    /** 模型显示名 */
    private String name;

    /** API Key */
    private String apiKey;

    /** API Base URL */
    private String baseUrl;

    /** 模型名称 */
    private String modelName;
}
