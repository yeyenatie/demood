package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dto.OrderCreateDTO;
import com.example.demo.dto.OrderItemDTO;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Orders;
import com.example.demo.mapper.OrdersMapper;
import com.example.demo.service.OrderItemService;
import com.example.demo.service.OrderService;
import com.example.demo.vo.OrderItemVo;
import com.example.demo.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrderService {
    @Autowired
    private OrderItemService orderItemService;

    //    创建订单,事务：主表和详情必须同时成功或失败，否则事务回滚
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderVo createOrder(OrderCreateDTO dto, Long userId) {
//        1.生成唯一订单号
        String orderNo = System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8);
//        2.计算总金额，构建订单详情
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItemList = new ArrayList<>();
        for (OrderItemDTO itemDTO : dto.getItemlist()) {
//            实际项目这里需要查询实际商品表校验价格，库存，防止篡改
            BigDecimal subTotal = itemDTO.getProductPrice().multiply(new BigDecimal(itemDTO.getNum()));
            totalAmount = totalAmount.add(subTotal);
            OrderItem item = new OrderItem();
            item.setProductId(itemDTO.getProductId());
            item.setProductPrice(itemDTO.getProductPrice().doubleValue());
            item.setNum(itemDTO.getNum());
            item.setSubTotal(subTotal.doubleValue());
            item.setProductName("商品名称");
            orderItemList.add(item);
        }
//        3.保存订单主表orders
        Orders order = new Orders();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalAmount(totalAmount.doubleValue());
        order.setOrderStatus(0);//待付款
        order.setReceiverName(dto.getReceiverName());
        order.setReceiverPhone(dto.getReceiverPhone());
        order.setAddress(dto.getAddress());
        this.save(order);
//        4.给所有订单详情设置orderId，批量保存订单详情
        for (OrderItem item : orderItemList) {
            item.setOrderId(order.getId());
        }
        orderItemService.saveBatch(orderItemList);
//        5.组装VO返回
        OrderVo VO = new OrderVo();
        VO.setId(order.getId());
        VO.setOrderNo(orderNo);
        VO.setUserId(userId);
        VO.setTotalAmount(totalAmount.doubleValue());
        VO.setOrderStatus(order.getOrderStatus());
        VO.setReceiverName(order.getReceiverName());
        VO.setReceiverPhone(order.getReceiverPhone());
        VO.setAddress(order.getAddress());
        VO.setCreateTime(order.getCreateTime());
        List<OrderItemVo> itemVOList = new ArrayList<>();
        for (OrderItem item : orderItemList) {
            OrderItemVo itemVO = new OrderItemVo();
            itemVO.setId(item.getId());
            itemVO.setOrderId(item.getOrderId());
            itemVO.setProductId(item.getProductId());
            itemVO.setProductName(item.getProductName());
            itemVO.setProductPrice(item.getProductPrice());
            itemVO.setNum(item.getNum());
            itemVO.setSubTotal(item.getSubTotal());
            itemVOList.add(itemVO);
        }
        VO.setItemList(itemVOList);
        return VO;
    }

    //    获取全部订单信息
    @Override
    public List<OrderVo> getOrderListByUserId(Long userId) {
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId, userId);
        List<Orders> orderList = this.list(wrapper);
        List<OrderVo> result = new ArrayList<>();
        for (Orders order : orderList) {
            OrderVo VO = new OrderVo();
            VO.setId(order.getId());
            VO.setOrderNo(order.getOrderNo());
            VO.setUserId(order.getUserId());
            VO.setTotalAmount(order.getTotalAmount());
            VO.setOrderStatus(order.getOrderStatus());
            VO.setReceiverName(order.getReceiverName());
            VO.setReceiverPhone(order.getReceiverPhone());
            VO.setAddress(order.getAddress());
            VO.setCreateTime(order.getCreateTime());

            //查询该订单下所有详情
            LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
            itemWrapper.eq(OrderItem::getOrderId, order.getId());
            List<OrderItem> itemList = orderItemService.list(itemWrapper);
            List<OrderItemVo> itemVOList = new ArrayList<>();
            for (OrderItem item : itemList) {
                OrderItemVo itemVO = new OrderItemVo();
                itemVO.setId(item.getId());
                itemVO.setOrderId(item.getOrderId());
                itemVO.setProductId(item.getProductId());
                itemVO.setProductName(item.getProductName());
                itemVO.setProductPrice(item.getProductPrice());
                itemVO.setNum(item.getNum());
                itemVO.setSubTotal(item.getSubTotal());
                itemVOList.add(itemVO);
            }
            VO.setItemList(itemVOList);
            result.add(VO);
        }
        return result;
    }

    //    根据订单号查询订单详情
    @Override
    public OrderVo getOrderDetail(String orderNo, Long userId) {
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getOrderNo, orderNo);
        wrapper.eq(Orders::getUserId, userId);
        Orders order = this.getOne(wrapper);
        if (order == null || !order.getUserId().equals(userId)) {
            return null;
        }
        OrderVo VO = new OrderVo();
        VO.setId(order.getId());
        VO.setOrderNo(order.getOrderNo());
        VO.setUserId(order.getUserId());
        VO.setTotalAmount(order.getTotalAmount());
        VO.setOrderStatus(order.getOrderStatus());
        VO.setReceiverName(order.getReceiverName());
        VO.setReceiverPhone(order.getReceiverPhone());
        VO.setAddress(order.getAddress());
        VO.setCreateTime(order.getCreateTime());
        LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(OrderItem::getOrderId, order.getId());
        List<OrderItem> itemList = orderItemService.list(itemWrapper);
        List<OrderItemVo> itemVOList = new ArrayList<>();
        for (OrderItem item : itemList) {
            OrderItemVo itemVO = new OrderItemVo();
            itemVO.setId(item.getId());
            itemVO.setOrderId(item.getOrderId());
            itemVO.setProductId(item.getProductId());
            itemVO.setProductName(item.getProductName());
            itemVO.setProductPrice(item.getProductPrice());
            itemVO.setNum(item.getNum());
            itemVO.setSubTotal(item.getSubTotal());
            itemVOList.add(itemVO);
        }
        VO.setItemList(itemVOList);
        return VO;
    }

    //    取消订单
    @Override
    public boolean cancelOrder(String orderNo, Long userId) {
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getOrderNo, orderNo);
        wrapper.eq(Orders::getUserId, userId);
        Orders order = this.getOne(wrapper);
        if (order == null || !order.getUserId().equals(userId)) {
            return false;
        }

        //只能取消待付款订单
        if (order.getOrderStatus() != 0) {
            return false;
        }
        order.setOrderStatus(4); // 4=已取消
        return this.updateById(order);
    }
}
