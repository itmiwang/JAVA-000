package com.miwang.learn.spring.data.mutiple.datasource.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author guozq
 * @date 2020-12-08-12:20 上午
 */
@Data
public class Order {
    private Integer id;
    
    private String orderSn;
    
    private Integer customerId;
    
    private Short orderStatus;
    
    private Date createTime;
    
    private Date shipTime;
    
    private Date payTime;
    
    private Date receiveTime;
    
    private BigDecimal discountMoney;
    
    private BigDecimal shipMoney;
    
    private BigDecimal payMoney;
    
    private Short payMethod;
    
    private String address;
    
    private String receiveUser;
    
    private String shipSn;
    
    private String shipCompanyName;
    
}
