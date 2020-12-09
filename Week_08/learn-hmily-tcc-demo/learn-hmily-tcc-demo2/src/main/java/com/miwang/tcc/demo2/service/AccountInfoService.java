package com.miwang.tcc.demo2.service;

/**
 * @author guozq
 * @date 2020-12-09-4:54 下午
 */
public interface AccountInfoService {

    //账户扣款
    public  void updateAccountBalance(String accountNo, Double amount);
}
