package com.example.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.dto.ProductDTO;
import com.example.demo.service.ProductService;
import com.example.demo.vo.ProductVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@Tag(name = "商品管理接口")
public class ProductController {
    @Resource
    private ProductService productService;

    //    新增或修改商品
    @PostMapping("/save")
    @Operation(summary = "新增或修改商品")
    public String saveProduct(@Valid @RequestBody ProductDTO productDTO) {
        productService.saveProduct(productDTO);
        return "操作成功";
    }

    //    分页查询
    @GetMapping("/page")
    @Operation(summary = "分页查询商品")
    public IPage<ProductVO> pageProduct(@RequestParam(defaultValue = "1") long pageNum, @RequestParam(defaultValue = "6") long pageSize, @RequestParam(required = false) String productName) {
        return productService.pageProduct(pageNum, pageSize, productName);
    }

    //    根据id查询商品
    @GetMapping("/{id}")
    @Operation(summary = "根据id查询商品")
    public ProductVO getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    //    逻辑删除商品
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "逻辑删除商品")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "删除成功";
    }
}
