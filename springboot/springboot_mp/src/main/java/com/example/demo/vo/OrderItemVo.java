package com.example.demo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "订单详情返回Vo")
public class OrderItemVo {

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

}
