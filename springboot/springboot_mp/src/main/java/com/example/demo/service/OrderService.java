package com.example.demo.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.dto.OrderCreateDTO;
import com.example.demo.entity.Orders;
import com.example.demo.vo.OrderVo;

import java.util.List;

//订单服务接口
public interface OrderService extends IService<Orders> {

    //创建订单
    OrderVo createOrder(OrderCreateDTO dto, Long userId);

    //获取全部订单信息
    List<OrderVo> getOrderListByUserId(Long userId);

    //根据订单号查询订单详情
    OrderVo getOrderDetail(String orderNo, Long userId);

    //取消订单
    boolean cancelOrder(String orderNo, Long userId);

}
