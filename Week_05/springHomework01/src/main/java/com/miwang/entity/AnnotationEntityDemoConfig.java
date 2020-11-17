package com.miwang.entity;

import org.springframework.context.annotation.ComponentScan;

/**
 * @author guozq
 * @date 2020-11-17-4:56 下午
 */
//@ComponentScan
//@ComponentScan(basePackages = "com.miwang.entity")
@ComponentScan(basePackageClasses = com.miwang.entity.AnnotationEntityDemo.class)
public class AnnotationEntityDemoConfig {
}
