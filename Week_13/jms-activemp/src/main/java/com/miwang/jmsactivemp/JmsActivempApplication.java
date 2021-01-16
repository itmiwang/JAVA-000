package com.miwang.jmsactivemp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class JmsActivempApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(JmsActivempApplication.class, args);
    }
    
}
