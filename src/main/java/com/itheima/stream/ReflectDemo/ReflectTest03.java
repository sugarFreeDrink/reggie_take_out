package com.itheima.stream.ReflectDemo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ReflectTest03 {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> c = Class.forName("com.itheima.stream.ReflectDemo.Student");
        Constructor<?> con = c.getConstructor();
        Object obj = con.newInstance();
        Field address = c.getField("address");
        Field name = c.getDeclaredField("name");
        Field age = c.getDeclaredField("age");
        name.setAccessible(true);
        address.set(obj,"重庆");
        System.out.println(obj);
        name.set(obj,"王祖贤");
        age.set(obj,12);
        System.out.println(obj);


    }
}
