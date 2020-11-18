package com.miwang.springHomework02;

import com.miwang.springHomework02.entity.Student;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author guozq
 * @date 2020-11-18-6:57 上午
 */
@EnableAutoConfiguration
public class EnableAutoConfigurationTest {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(EnableAutoConfigurationTest.class)
                .web(WebApplicationType.NONE)
                .run(args);
        Student student = context.getBean(Student.class);
        student.init();
        student = student.create();
        System.out.println(student);
        context.close();
    }
}
