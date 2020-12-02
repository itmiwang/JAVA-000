package com.miwang.shardingsphere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.miwang.shardingsphere.mapper")
public class ApacheShardingsphereDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApacheShardingsphereDemoApplication.class, args);
	}

}
