package com.itheima.stream.ReflectDemo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectTest04 {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> c = Class.forName("com.itheima.stream.ReflectDemo.Student");
        Constructor<?> con = c.getConstructor();
        Object obj = con.newInstance();

        Method method1 = c.getMethod("method1");
        method1.invoke(obj);

        Method method2 = c.getMethod("method2", String.class);
        method2.invoke(obj, "林宥嘉");

        Method method3 = c.getMethod("method3", String.class, int.class);
        String str = (String) method3.invoke(obj, "王祖贤", 30);
        System.out.println(str);

        Method function = c.getDeclaredMethod("function");
        function.setAccessible(true);
        function.invoke(obj);
    }
}
