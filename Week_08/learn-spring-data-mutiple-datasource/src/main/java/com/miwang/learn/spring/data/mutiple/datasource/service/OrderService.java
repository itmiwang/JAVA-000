package com.miwang.learn.spring.data.mutiple.datasource.service;

import com.miwang.learn.spring.data.mutiple.datasource.entity.Order;

import java.util.List;

/**
 * 订单服务
 * @author guozq
 * @date 2020-12-08-12:44 上午
 */
public interface OrderService {
    /**
     * 批量插入订单数据
     */
    int[] batchInsert(final List<Order> orders);
    
    /**
     * 插入订单数据
     */
    int insert(final Order order);
    
    /**
     * 查询订单
     */
    Order selectById(int id);
}
