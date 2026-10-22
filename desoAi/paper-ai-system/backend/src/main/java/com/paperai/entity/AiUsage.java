package com.paperai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * AI 生成次数配额（按用户 + 周）
 */
@Data
@TableName("ai_usage")
public class AiUsage {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 所属用户 ID */
    private Long userId;

    /** 周标识，如 2026-W42 */
    private String weekKey;

    /** 本周已用次数 */
    private Integer count;

    private LocalDateTime updateTime;
}
