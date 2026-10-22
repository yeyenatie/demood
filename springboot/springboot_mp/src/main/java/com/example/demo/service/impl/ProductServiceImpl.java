package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.convert.ProductConvert;
import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Product;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.service.ProductService;
import com.example.demo.vo.ProductVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    //    新增，修改商品
    @Override
    public boolean saveProduct(ProductDTO productDTO) {
//        MapStruct自动把DTO转Entity
        Product product = ProductConvert.INSTANCE.dtoToEntity(productDTO);
        return saveOrUpdate(product);
    }

    //    分页查询
    @Override
    public IPage<ProductVO> pageProduct(long pageNum, long pageSize, String productName) {
//        构建分页对象
        IPage<Product> page = new Page<>(pageNum, pageSize);
//        条件构造器：按名称模糊查询+按创建时间查询
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(productName), Product::getProductName, productName).orderByDesc(Product::getCreateTime);
//        分页查询
        this.page(page, wrapper);
//        page.convert:分页记录中每条记录转VO，返回新的page
        return page.convert(ProductConvert.INSTANCE::entityToVO);
    }

    //    根据id查询商品
    @Override
    public ProductVO getProductById(Long id) {
        Product product = getById(id);
        if (product == null) {
            return null;
        }
        return ProductConvert.INSTANCE.entityToVO(product);
    }

    //    逻辑删除
    @Override
    public boolean deleteProduct(Long id) {
        return removeById(id);
    }
}
