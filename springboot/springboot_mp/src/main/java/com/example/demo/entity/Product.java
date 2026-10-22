package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("product")
@Data
@Schema(description = "商品实体")
public class Product {
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
    //    商品图片相对路径：images/XXX.jpg
//    前端访问:http://localhost:8080/images/XXX.jpg(由静态资源映射解析到E:\gongzhuo\product_images）
    @Schema(description = "商品图片")
    private String productImage;
    @Schema(description = "商品状态0:下架1:上架")
    private Integer status;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    @TableLogic
    @Schema(description = "逻辑删除标记 0:未删除 1:已删除")
    private Integer deleted;
}
