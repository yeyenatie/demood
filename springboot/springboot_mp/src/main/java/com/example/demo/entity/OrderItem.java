package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("order_item")
@Data
@Schema(description = "订单详情")
public class OrderItem {

    @TableId(type = IdType.AUTO)
    @Schema(description = "订单详情ID")
    private Long id;

    @Schema(description = "订单ID，关联订单主表")
    private Long orderId;

    @Schema(description = "商品ID，关联商品主表")
    private Long productId;

    @Schema(description = "下单时商品名称")
    private String productName;

    @Schema(description = "下单时商品单价")
    private Double productPrice;

    @Schema(description = "购买数量")
    private int num;

    @Schema(description = "小计金额")
    private Double subTotal;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableLogic
    @Schema(description = "逻辑删除标记 0:未删除 1:已删除")
    private Integer deleted;

}
