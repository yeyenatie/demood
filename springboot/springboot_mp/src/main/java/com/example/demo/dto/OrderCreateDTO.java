package com.example.demo.dto;

//创建订单DTO，前端下单时提交的数据

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "创建订单请求DTO")
public class OrderCreateDTO {

    @NotBlank(message = "收货人不能为空")
    @Schema(description = "收货人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String receiverName;

    @NotBlank(message = "收货手机号不能为空")
    @Schema(description = "收货手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String receiverPhone;

    @NotBlank(message = "收货地址不能为空")
    @Schema(description = "收货地址", requiredMode = Schema.RequiredMode.REQUIRED)
    private String address;

    @NotEmpty(message = "订单商品不能为空")
    @Schema(description = "购买商品列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<OrderItemDTO> itemlist;
}
