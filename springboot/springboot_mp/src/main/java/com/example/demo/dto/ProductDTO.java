package com.example.demo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "商品新增，修改")
public class ProductDTO {
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键id，修改时传入")
    private Long id;
    @Schema(description = "商品名称不能为空")
    private String productName;
    @Schema(description = "商品单价不能为空")
    private BigDecimal price;
    @Schema(description = "库存数量")
    private Integer stock;
    @Schema(description = "商品描述")
    private String productDesc;
    //    商品图片相对路径：images/XXX.jpg
//    前端访问:http://localhost:8080/images/XXX.jpg(由静态资源映射解析到E:\gongzhuo\product_images）
    @Schema(description = "商品图片")
    private String productImage;
    @Schema(description = "商品状态0:下架1:上架")
    private Integer status;
}
