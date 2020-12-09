package com.miwang.tcc.demo1.service;

/**
 * @author guozq
 * @date 2020-12-09-11:04 上午
 */
public interface AccountInfoService {

    //账户扣款
    public void updateAccountBalance(String accountNo, Double amount);
}
