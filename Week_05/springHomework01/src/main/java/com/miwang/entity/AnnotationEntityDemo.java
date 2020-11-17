package com.miwang.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author guozq
 * @date 2020-11-17-4:53 下午
 */
@Component(value = "annotationEntiryDemo")
public class AnnotationEntityDemo {

    @Value("1")
    private String id;

    @Value("王五")
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
        return "AnnotationEntityDemo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
