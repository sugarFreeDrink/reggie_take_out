package com.itheima.stream.ClassLoaderDemo;

public class Demo01 {
    public static void main(String[] args) {
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println(systemClassLoader);

        ClassLoader parent = systemClassLoader.getParent();
        System.out.println(parent);

        ClassLoader parent2 = parent.getParent();
        System.out.println(parent2);
    }
}
