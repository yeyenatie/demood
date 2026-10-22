package com.example.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Product;
import com.example.demo.vo.ProductVO;

public interface ProductService extends IService<Product> {
    //    新增或修改
    boolean saveProduct(ProductDTO productDTO);

    //    分页查询
    IPage<ProductVO> pageProduct(long pageNum, long pageSize, String productName);

    //    逻辑删除商品
    boolean deleteProduct(Long id);

    //    根据id查询商品
    ProductVO getProductById(Long id);
}
