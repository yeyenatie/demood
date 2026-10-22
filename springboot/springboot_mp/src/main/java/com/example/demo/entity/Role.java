package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("role")
@Data
@Schema(description = "角色实体")
public class Role {
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键id")
    private Long id;
    @Schema(description = "角色名")
    private String roleName;
    @Schema(description = "角色编码")
    private String roleCode;
    @Schema(description = "角色描述")
    private String description;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    @TableLogic
    @Schema(description = "逻辑删除标记 0:未删除 1:已删除")
    private Integer deleted;
}
