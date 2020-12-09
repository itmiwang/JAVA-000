package com.miwang.tcc.demo1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author guozq
 * @date 2020-12-09-11:04 上午
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix
@EnableAspectJAutoProxy
@EnableFeignClients(basePackages = {"com.miwang.tcc.demo1.spring"})
@ComponentScan({"com.miwang.tcc.demo1", "org.dromara.hmily"})
public class LearnHmilyTccDemo1Application {
    public static void main(final String[] args) {
        SpringApplication.run(LearnHmilyTccDemo1Application.class, args);
    }
}
