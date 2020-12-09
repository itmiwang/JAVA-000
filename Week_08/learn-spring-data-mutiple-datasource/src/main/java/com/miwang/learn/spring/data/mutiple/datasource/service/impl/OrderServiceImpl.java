package com.miwang.learn.spring.data.mutiple.datasource.service.impl;

import com.miwang.learn.spring.data.mutiple.datasource.entity.Order;
import com.miwang.learn.spring.data.mutiple.datasource.mapper.OrderMapper;
import com.miwang.learn.spring.data.mutiple.datasource.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 订单服务
 * @author guozq
 * @date 2020-12-08-12:45 上午
 */
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderMapper mapper;
    
    @Override
    public int[] batchInsert(final List<Order> orders) {
        return new int[0];
    }
    
    @Override
    public int insert(final Order order) {
        return 0;
    }
    
    @Override
    public Order selectById(final int id) {
        return null;
    }
}
