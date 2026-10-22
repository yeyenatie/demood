package com.paperai.dto;

import lombok.Data;

/**
 * 论文创建 / 保存请求
 */
@Data
public class PaperSaveRequest {

    private String title;

    private String keywords;

    private String paperType;

    private String researchDirection;

    /** 大纲（JSON 字符串） */
    private String outline;

    /** 正文内容（JSON 字符串） */
    private String content;

    /** 状态：draft / done */
    private String status;
}
