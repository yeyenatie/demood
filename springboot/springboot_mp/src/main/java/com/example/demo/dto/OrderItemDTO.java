package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "订单商品项DTO")
public class OrderItemDTO {

    @NotNull(message = "商品ID不能为空")
    @Schema(description = "商品ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long productId;

    @NotNull(message = "商品数量不能为空")
    @Schema(description = "商品数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer num;

    @NotNull(message = "商品单价不能为空")
    @Schema(description = "下单时商品单价", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal productPrice;

}
