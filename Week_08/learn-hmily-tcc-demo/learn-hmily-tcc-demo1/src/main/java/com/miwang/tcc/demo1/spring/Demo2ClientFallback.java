package com.miwang.tcc.demo1.spring;

import org.springframework.stereotype.Component;

/**
 * @author guozq
 * @date 2020-12-09-11:04 上午
 */
@Component
public class Demo2ClientFallback implements Demo2Client {

    @Override
    public Boolean transfer(Double amount) {

        return false;
    }
}
