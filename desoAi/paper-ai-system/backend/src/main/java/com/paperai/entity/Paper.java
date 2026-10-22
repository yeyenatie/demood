package com.paperai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 论文实体
 */
@Data
@TableName("paper")
public class Paper {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 所属用户 ID */
    private Long userId;

    /** 论文题目 */
    private String title;

    /** 关键词 */
    private String keywords;

    /** 论文类型：毕业论文 / 期刊论文 / 课程论文 */
    private String paperType;

    /** 研究方向描述 */
    private String researchDirection;

    /** 大纲（JSON 字符串） */
    private String outline;

    /** 正文内容（JSON 字符串） */
    private String content;

    /** 状态：draft-草稿 done-已完成 */
    private String status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
