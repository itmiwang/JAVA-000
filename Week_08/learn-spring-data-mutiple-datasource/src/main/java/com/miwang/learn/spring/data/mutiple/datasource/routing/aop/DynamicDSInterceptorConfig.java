package com.miwang.learn.spring.data.mutiple.datasource.routing.aop;

import com.miwang.learn.spring.data.mutiple.datasource.routing.annotation.DynamicDS;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.context.annotation.Bean;

/**
 * 动态数据源拦截器配置类，配置切点和拦截器
 * @author guozq
 * @date 2020-12-08-12:38 上午
 */
public class DynamicDSInterceptorConfig {
    @Bean
    public DynamicDSAnnotationInterceptor dynamicDSAnnotationInterceptor() {
        return new DynamicDSAnnotationInterceptor();
    }
    
    @Bean
    public Advisor dynamicDSAnnotationAdvisor(DynamicDSAnnotationInterceptor dynamicDSAnnotationInterceptor) {
        // 定义注解切点
        AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(null, DynamicDS.class);
        DefaultPointcutAdvisor dynamicDSAnnotationAdvisor = new DefaultPointcutAdvisor();
        dynamicDSAnnotationAdvisor.setPointcut(pointcut);
        dynamicDSAnnotationAdvisor.setAdvice(dynamicDSAnnotationInterceptor);
        return dynamicDSAnnotationAdvisor;
    }
}
