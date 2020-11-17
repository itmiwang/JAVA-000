package com.miwang.service;

import com.miwang.dao.UserDao;
import com.miwang.entity.AutoEntityDemo;
import com.miwang.service.AutoEntityDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author guozq
 * @date 2020-11-17-2:23 下午
 */
@Component("autoEntityDemoService")
public class AutoEntityDemoServiceImpl implements AutoEntityDemoService {

    @Autowired
    private AutoEntityDemo autoEntityDemo;

    @Override
    public void printInfo() {
        System.out.println("id值为：" + autoEntityDemo.getId());
        System.out.println("name值为：" + autoEntityDemo.getName());
    }
}
