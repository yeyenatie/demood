package com.paperai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户自配 AI 模型（每个用户可添加多个，生成时选择使用哪个）
 */
@Data
@TableName("ai_model")
public class AiModel {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 所属用户 ID */
    private Long userId;

    /** 模型显示名，如：我的 DeepSeek */
    private String name;

    /** API Key */
    private String apiKey;

    /** API Base URL */
    private String baseUrl;

    /** 模型名称，如 deepseek-chat */
    private String modelName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
