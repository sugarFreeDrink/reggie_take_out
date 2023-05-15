package com.itheima.stream;

import java.util.ArrayList;
import java.util.stream.Stream;

public class StreamDemo02 {
    public static void main(String[] args) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("张曼玉");
        arrayList.add("张敏");
        arrayList.add("林宥嘉");
        arrayList.add("周星驰");
        arrayList.add("abc");
        arrayList.add("bcde");

        arrayList.stream().filter((s ->
                s.startsWith("张")
        )).forEach(System.out::println);
        System.out.println("-----------------");
        arrayList.stream().filter(s -> s.startsWith("张")).filter(a->a.length()==3).forEach(System.out::println);
        //跳过两个元素 输出第一个
        System.out.println("---------------");
        arrayList.stream().skip(2).limit(1).forEach(System.out::println);

        System.out.println("----------");
        //合并需求1和2
        Stream<String> s1 = arrayList.stream().limit(3);
        Stream<String> s2 = arrayList.stream().skip(2);
        Stream<String> s3 = arrayList.stream().skip(2);
     //   Stream.concat(s1,s3).forEach(System.out::println);
        System.out.println("----------");
        Stream.concat(s1,s2).distinct().forEach(System.out::println);
        System.out.println("----------");
        //按照字母顺序 暑促和
        arrayList.stream().sorted().forEach(System.out::println);

        System.out.println("----------");
        //按照字符串长度比较
        arrayList.stream().sorted();
    }
}
