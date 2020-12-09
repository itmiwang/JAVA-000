package com.miwang.learn.spring.data.mutiple.datasource.routing;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 数据源路由配置
 * @author guozq
 * @date 2020-12-08-12:24 上午
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
    
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDSContextHolder.peek();
    }
}
