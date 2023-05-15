package com.itheima.stream;

import java.util.*;
import java.util.stream.Stream;

public class StreamDemo {
    public static void main(String[] args) {

        //数组生成流
        String[] strArray={"","haha"};
        Stream<String> strArray1 = Stream.of(strArray);
        Stream.of("","hello");
        //Map间接生成流

        Map<String,Integer> map =new HashMap<String,Integer>(   );
        Stream<String> keyStream = map.keySet().stream();
        Stream<Integer> valueStream = map.values().stream();
        Stream<Map.Entry<String, Integer>> entryStream = map.entrySet().stream();
        //collection体系的集合可以用默认方法生成流
        //set生成流
        Set<String> set = new HashSet<>();
        Stream<String> stream = set.stream();
        //list生成流
        List<String> list = new ArrayList<>();
        Stream<String> listStream = list.stream();

    }
}
