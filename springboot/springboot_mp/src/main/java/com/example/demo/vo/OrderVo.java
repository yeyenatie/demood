package com.example.demo.vo;

//订单Vo,返回给前端的订单信息+商品明细

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "订单返回Vo(含订单明细)")
public class OrderVo {

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

    @Schema(description = "订单商品明细列表")
    private List<OrderItemVo> itemList;
}
