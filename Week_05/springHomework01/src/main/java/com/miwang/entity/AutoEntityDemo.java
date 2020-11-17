package com.miwang.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author guozq
 * @date 2020-11-17-5:43 下午
 */
@Component(value = "autoEntityDemo")
public class AutoEntityDemo {

    @Value("2")
    private String id;

    @Value("赵六")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AutoEntityDemo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
