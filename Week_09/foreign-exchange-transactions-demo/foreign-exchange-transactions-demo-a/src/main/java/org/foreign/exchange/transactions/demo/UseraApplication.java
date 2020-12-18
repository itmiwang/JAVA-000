package org.foreign.exchange.transactions.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @author guozq
 * @date 2020-12-18-3:39 上午
 */
@SpringBootApplication
@ImportResource({"classpath:applicationContext.xml"})
public class UseraApplication {
    public static void main(final String[] args) {
        SpringApplication springApplication = new SpringApplication(UseraApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}

