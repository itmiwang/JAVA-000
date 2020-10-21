package com.miwang.practice01;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author guozq
 * @date 2020-10-20-1:36 上午
 */
public class MyClassLoader extends ClassLoader {
    public static void main(String[] args) {
        try {
            Object myClassObj = new MyClassLoader().findClass("Hello").newInstance();
            Method myClassMethod = myClassObj.getClass().getMethod("hello");
            myClassMethod.invoke(myClassObj);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            Path path = Paths.get("/Users/miwang/javaWeb/advancement-workspace/homework整理/01/Hello/Hello.xlass");
            byte[] bytes = Files.readAllBytes(path);
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) (255 - bytes[i]);
            }
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException();
        }
    }
}

