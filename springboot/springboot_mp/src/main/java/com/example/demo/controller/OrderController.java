package com.example.demo.controller;

//订单控制器,强制JWT登录，浏览商品不需要

import com.example.demo.common.Result;
import com.example.demo.dto.OrderCreateDTO;
import com.example.demo.service.OrderService;
import com.example.demo.util.JwtUtil;
import com.example.demo.vo.OrderVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@Tag(name = "订单模块", description = "订单创建、查询、取消接口，创建订单需要登录")
public class OrderController {

    @Resource
    private OrderService orderService;

    @Resource
    private JwtUtil jwtUtil;

    //创建订单，必须登录
    @PostMapping("/create")
    @Operation(summary = "创建订单", description = "提交下单，需要携带token，强制登录")
    public Result<OrderVo> createOrder(@RequestHeader("Authorization")
                                       @Parameter(description = "JWT令牌,格式:Bearer XXX") String token,
                                       @Valid @RequestBody
                                       @Parameter(description = "下单表单数据") OrderCreateDTO dto) {
        String realToken = token.replace("Bearer ", "");
        //解析token拿到userId
        Long userId = jwtUtil.getUserId(realToken);
        OrderVo orderVo = orderService.createOrder(dto, userId);
        return Result.success(orderVo);
    }

    //获取我的订单列表
    @GetMapping("/list")
    @Operation(summary = "获取我的订单列表", description = "查询当前用户的所有订单")
    public Result<List<OrderVo>> getOrderList(@RequestHeader("Authorization")
                                              @Parameter(description = "JWT令牌,格式:Bearer XXX") String token) {
        String realToken = token.replace("Bearer ", "");
        Long userId = jwtUtil.getUserId(realToken);
        List<OrderVo> orderList = orderService.getOrderListByUserId(userId);
        return Result.success(orderList);
    }

    //根据订单号查询订单信息
    @GetMapping("/detail/{orderNo}")
    @Operation(summary = "订单详情", description = "通过订单编号查询订单详情，校验归属用户，防止越权")
    public Result<OrderVo> getOrderDetail(@RequestHeader("Authorization")
                                          @Parameter(description = "JWT令牌,格式:Bearer XXX") String token,
                                          @PathVariable @Parameter(description = "订单编号") String orderNo) {
        String realToken = token.replace("Bearer ", "");
        Long userId = jwtUtil.getUserId(realToken);
        OrderVo orderVo = orderService.getOrderDetail(orderNo, userId);
        return Result.success(orderVo);
    }

    //取消订单
    @PutMapping("/cancel/{orderNo}")
    @Operation(summary = "取消订单", description = "仅待付款订单可以取消")
    public Result<Boolean> cancelOrder(@RequestHeader("Authorization")
                                       @Parameter(description = "JWT令牌,格式:Bearer XXX") String token,
                                       @PathVariable @Parameter(description = "订单编号") String orderNo) {
        String realToken = token.replace("Bearer ", "");
        Long userId = jwtUtil.getUserId(realToken);
        boolean ok = orderService.cancelOrder(orderNo, userId);
        return Result.success(ok);
    }
}
