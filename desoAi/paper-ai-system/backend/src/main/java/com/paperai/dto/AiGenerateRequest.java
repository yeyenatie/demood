package com.paperai.dto;

import lombok.Data;

/**
 * AI 生成相关请求（题目推荐 / 大纲生成 / 章节生成共用）
 */
@Data
public class AiGenerateRequest {

    /** 研究方向描述 */
    private String researchDirection;

    /** 论文类型 */
    private String paperType = "毕业论文";

    /** 论文题目 */
    private String title;

    /** 关键词 */
    private String keywords;

    /** 章节名称，如：1 绪论 */
    private String sectionName;

    /** 大纲要点 */
    private String points;

    /** 前文内容（用于保持行文连贯） */
    private String context;

    /** 论文 ID（大纲生成时用于自动保存） */
    private Long paperId;

    /** 使用的用户自配模型 ID；为空则使用默认模型（受每周次数限制） */
    private Long modelId;
}
