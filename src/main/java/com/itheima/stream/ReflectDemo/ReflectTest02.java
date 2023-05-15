package com.itheima.stream.ReflectDemo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ReflectTest02
{
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        Class<?> c = Class.forName("com.itheima.stream.ReflectDemo.Student");
        Constructor<?>[] cons = c.getDeclaredConstructors();
        for(Constructor con:cons){
            System.out.println(con);
        }
        Object obj = c.newInstance();
        System.out.println(obj);
        Student student = new Student();
        System.out.println(student);

        System.out.println("---------------");
        Constructor<?> con = c.getConstructor(String.class, int.class, String.class);
        Object obj2 = con.newInstance("参数1",2,"参数3");
        System.out.println(obj2);

        System.out.println("---------------");
        Constructor<?> con2 = c.getDeclaredConstructor(String.class);
        con2.setAccessible(true);
        Object obj3 = con2.newInstance("参数名字");
        System.out.println(obj3);

        System.out.println("---------------");
        Field[] fields = c.getFields();
        for (Field field:fields){
            System.out.println(field);
        }

        System.out.println("---------------");
        Field[] declaredFields = c.getDeclaredFields();
        for (Field field:declaredFields){
            System.out.println(field);
        }

        System.out.println("---------------");
        Field field = c.getField("address");
        Constructor<?> con3 = c.getConstructor();
        Object obj4 = con3.newInstance();
        field.set(obj4,"武汉");
        System.out.println(obj4);


    }
}
