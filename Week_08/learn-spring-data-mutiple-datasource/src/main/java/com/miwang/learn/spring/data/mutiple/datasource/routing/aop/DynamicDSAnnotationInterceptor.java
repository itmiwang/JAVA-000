package com.miwang.learn.spring.data.mutiple.datasource.routing.aop;

import com.miwang.learn.spring.data.mutiple.datasource.routing.DynamicDSContextHolder;
import com.miwang.learn.spring.data.mutiple.datasource.routing.annotation.DynamicDS;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 动态数据源注解拦截器
 * @author guozq
 * @date 2020-12-08-12:36 上午
 */
@Slf4j
public class DynamicDSAnnotationInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(final MethodInvocation methodInvocation) throws Throwable {
        try {
            String dsFlag = methodInvocation.getMethod().getAnnotation(DynamicDS.class).value();
            DynamicDSContextHolder.push(dsFlag);
            return methodInvocation.proceed();
        } finally {
            if (DynamicDSContextHolder.empty()) {
                DynamicDSContextHolder.remove();
            } else {
                DynamicDSContextHolder.poll();
            }
        }
        
    }
}
