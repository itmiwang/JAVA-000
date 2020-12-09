package com.miwang.tcc.demo2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author guozq
 * @date 2020-12-09-4:54 下午
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix
@EnableAspectJAutoProxy
@ComponentScan({"com.miwang.tcc.demo2","org.dromara.hmily"})
public class LearnHmilyTccDemo2Application {
    public static void main(final String[] args) {
        SpringApplication.run(LearnHmilyTccDemo2Application.class, args);
    }
}
