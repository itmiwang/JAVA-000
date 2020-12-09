package com.miwang.tcc.demo1.controller;

import com.miwang.tcc.demo1.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author guozq
 * @date 2020-12-09-11:04 上午
 */
@RestController
public class Demo1Controller {
    @Autowired
    AccountInfoService accountInfoService;

    @RequestMapping("/transfer")
    public Boolean transfer(@RequestParam("amount") Double amount) {
        this.accountInfoService.updateAccountBalance("1", amount);
        return true;
    }

}
