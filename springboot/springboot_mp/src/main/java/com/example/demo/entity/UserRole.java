package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("user_role")
@Data
@Schema(description = "用户角色关联实体")
public class UserRole {
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键id")
    private Long id;
    @Schema(description = "用户id")
    private Long userId;
    @Schema(description = "角色id")
    private Long roleId;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @TableLogic
    @Schema(description = "逻辑删除标记 0:未删除 1:已删除")
    private Integer deleted;
}
