package org.foreign.exchange.transactions.demo.service.impl;

import org.foreign.exchange.transactions.demo.service.AssertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * @author guozq
 * @date 2020-12-18-4:15 下午
 */
public class AssertServiceImpl implements AssertService {
    private static final Logger LOG = LoggerFactory.getLogger(AssertServiceImpl.class);
    
    @Override
    public boolean ChangeDollar(final BigDecimal rmbCount) {
        return false;
    }
}
