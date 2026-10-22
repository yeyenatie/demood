package com.paperai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户自设 AI 模型配置
 */
@Data
@TableName("ai_setting")
public class AiSetting {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 所属用户 ID */
    private Long userId;

    /** 大模型 API Key */
    private String apiKey;

    /** API Base URL */
    private String baseUrl;

    /** 模型名称 */
    private String model;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
