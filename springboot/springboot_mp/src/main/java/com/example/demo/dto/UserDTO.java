package com.example.demo.dto;

// 注册登录，用户新增修改DTO

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "用户新增修改DTO")
public class UserDTO {
    @Schema(description = "用户ID，修改时传入")
    private Long id;
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Schema(description = "明文密码")
    private String password;
    @Schema(description = "昵称，注册选填")
    private String nickname;
    @Schema(description = "手机号，注册选填")
    private String phone;
    @Schema(description = "邮箱，注册选填")
    private String email;
}
