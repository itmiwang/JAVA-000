package com.miwang.shardingsphere.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * p_order
 * @author 
 */
@Data
public class POrder implements Serializable {
    /**
     * ID
     */
    private Long id;

    /**
     * 订单号
     */
    private Long serialNo;

    /**
     * 订单金额（单位：元）
     */
    private BigDecimal predictSum;

    /**
     * 优惠金额（单位：元）
     */
    private BigDecimal discountsSum;

    /**
     * 运费金额（单位：元）
     */
    private BigDecimal logisticsSum;

    /**
     * 实付金额（单位：元）
     */
    private BigDecimal actualSum;

    /**
     * 关联支付方式表
     */
    private Long payMethodId;

    /**
     * 关联物流表
     */
    private Long logisticsId;

    /**
     * 订单状态（）
     */
    private Integer orderState;

    /**
     * 物流状态（）
     */
    private Integer logisticsState;

    /**
     * 下单用户ID（关联user表ID）
     */
    private Long operatorId;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 上传人ID
     */
    private Long operationId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date changeTime;

    /**
     * 发货时间
     */
    private Date logisticsTime;

    /**
     * 完成时间
     */
    private Date successTime;

    /**
     * 逻辑删除标识（1：正常；2：删除）
     */
    private Integer sign;

    private static final long serialVersionUID = 1L;
}