package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("orders")
@Data
@Schema(description = "订单实体")

public class Orders {
    @TableId(type = IdType.AUTO)
    @Schema(description = "订单id")
    private Long id;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "下单用户id")
    private Long userId;

    @Schema(description = "订单总金额")
    private Double totalAmount;

    @Schema(description = "订单状态  0待付款 1已付款 2已发货 3已完成 4已取消")
    private Integer orderStatus;

    @Schema(description = "收货人")
    private String receiverName;

    @Schema(description = "收货手机号")
    private String receiverPhone;

    @Schema(description = "收货地址")
    private String address;

    @Schema(description = "下单时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @TableLogic
    @Schema(description = "逻辑删除标记 0:未删除 1:已删除")
    private Integer deleted;
}
