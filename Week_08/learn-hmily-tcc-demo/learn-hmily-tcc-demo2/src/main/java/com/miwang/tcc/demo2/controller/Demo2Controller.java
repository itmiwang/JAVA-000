package com.miwang.tcc.demo2.controller;

import com.miwang.tcc.demo2.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author guozq
 * @date 2020-12-09-4:54 下午
 */
@RestController
public class Demo2Controller {
    @Autowired
    AccountInfoService accountInfoService;

    @RequestMapping("/transfer")
    public Boolean transfer(@RequestParam("amount") Double amount) {
        this.accountInfoService.updateAccountBalance("2", amount);
        return true;
    }

}
