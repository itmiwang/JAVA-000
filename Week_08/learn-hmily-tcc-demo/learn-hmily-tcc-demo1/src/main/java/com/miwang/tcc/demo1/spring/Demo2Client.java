package com.miwang.tcc.demo1.spring;

import org.dromara.hmily.annotation.Hmily;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author guozq
 * @date 2020-12-09-11:04 上午
 */
@FeignClient(value="learn-hmily-tcc-demo2",fallback= Demo2ClientFallback.class)
public interface Demo2Client {
    //远程调用李四的微服务
    @GetMapping("/demo2/transfer")
    @Hmily
    public Boolean transfer(@RequestParam("amount") Double amount);
}
