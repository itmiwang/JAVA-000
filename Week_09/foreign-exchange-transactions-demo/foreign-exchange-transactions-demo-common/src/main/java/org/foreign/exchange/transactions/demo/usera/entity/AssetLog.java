package org.foreign.exchange.transactions.demo.usera.entity;

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
 * @date 2020-12-18-2:29 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AssetLog {
    private static final Logger LOG = LoggerFactory.getLogger(AssetLog.class);
    
    private Integer id;
    
    private BigDecimal dollerAsset;
    
    private BigDecimal rmbAsset;
    
    private Integer status;
    
    private Date createTime;
    
    private Date updateTime;
}
