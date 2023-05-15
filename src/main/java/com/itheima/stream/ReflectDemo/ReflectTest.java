package com.itheima.stream.ReflectDemo;

public class ReflectTest {
    public static void main(String[] args) throws ClassNotFoundException {
        Class<Student> c1 = Student.class;
        System.out.println(c1);
        Student s = new Student();
        Class<? extends Student> c3 = s.getClass();
        System.out.println(c1==c3);
        System.out.println("--------------------");
        Class<?> c4 = Class.forName("com.itheima.stream.ReflectDemo.Student");
        System.out.println(c1==c4);
    }
}
