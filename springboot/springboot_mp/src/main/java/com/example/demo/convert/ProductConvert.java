package com.example.demo.convert;

import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Product;
import com.example.demo.vo.ProductVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

//商品对象转换接口
@Mapper
public interface ProductConvert {
    //    获取转换实体
    ProductConvert INSTANCE = Mappers.getMapper(ProductConvert.class);

    //    实体转VO
    ProductVO entityToVO(Product product);

    //    DTO转实体
    Product dtoToEntity(ProductDTO productDTO);

    //    实体转VO
    List<ProductVO> entityListToVOList(List<Product> productList);
}
