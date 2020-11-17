package com.miwang.entity;

import java.util.List;

/**
 * @author guozq
 * @date 2020-11-17-1:45 下午
 */
public class XmlEntityDemo {

    private String username;

    private String password;

    private List<String> list;


    public XmlEntityDemo(String username, String password, List<String> list) {
        this.username = username;
        this.password = password;
        this.list = list;
    }

    public XmlEntityDemo() {
        super();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "XmlEntityDemo{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", list=" + list +
                '}';
    }
}
