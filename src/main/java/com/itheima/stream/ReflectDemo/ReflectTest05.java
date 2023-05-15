package com.itheima.stream.ReflectDemo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Properties;

/*
* 往ArrayList<Integer>集合添加字符串 。*/
public class ReflectTest05 {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ClassNotFoundException, InstantiationException {
        ArrayList<Integer> array = new ArrayList<>();
        array.add(10);
        array.add(20);

        Class<? extends ArrayList> c = array.getClass();
        Method method = c.getMethod("add", Object.class);
        method.invoke(array,"参数1");
        System.out.println(array    );

        Properties properties = new Properties();
        FileReader fileReader = new FileReader("F:\\IdeaMavenProjects\\reggie_take_out\\class.txt");

        properties.load(fileReader);
        fileReader.close();
        String className = properties.getProperty("className");
        String methodName = properties.getProperty("methodName");

        System.out.println("-----------------------");
        Class<?> cla = Class.forName(className);
        Constructor<?> con = cla.getConstructor();
        Object obj = con.newInstance();
        Method m = cla.getMethod(methodName);
        m.invoke(obj);



    }
}
