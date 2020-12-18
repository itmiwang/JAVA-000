package org.foreign.exchange.transactions.demo.userb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author guozq
 * @date 2020-12-18-2:21 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Dollar {
    private static final Logger LOG = LoggerFactory.getLogger(Rmb.class);
    
    private Integer id;
    
    private BigDecimal asset;
    
    private Date createTime;
    
    private Date updateTime;
}
