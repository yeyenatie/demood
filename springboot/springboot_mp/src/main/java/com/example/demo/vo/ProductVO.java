package com.example.demo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "商品返回VO")
public class ProductVO {
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键id")
    private Long id;
    @Schema(description = "商品名称")
    private String productName;
    @Schema(description = "商品单价")
    private BigDecimal price;
    @Schema(description = "库存数量")
    private Integer stock;
    @Schema(description = "商品描述")
    private String productDesc;
    @Schema(description = "商品图片")
    private String productImage;
    @Schema(description = "商品状态0:下架1:上架")
    private Integer status;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
