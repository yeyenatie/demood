package com.example.demo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

//用户返回VO
//VO:返回给前端的数据，过滤敏感字段（如密码不返回）
@Data
@Schema(description = "用户VO")
public class UserVO {
    @Schema(description = "主键id")
    private Long id;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "昵称")
    private String nickname;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "状态 0:禁用 1:正常")
    private Integer status;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
