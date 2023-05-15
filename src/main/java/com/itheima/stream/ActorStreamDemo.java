package com.itheima.stream;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang.StringUtils.split;

public class ActorStreamDemo {
    public static void main(String[] args) {
        ArrayList<String> manList = new ArrayList<>();
        manList.add("周润发");
        manList.add("刘德华");
        manList.add("成龙");
        manList.add("周星驰");
        manList.add("李连杰");

        ArrayList<String> womanList = new ArrayList<>();
        womanList.add("林心如");
        womanList.add("张曼玉");
        womanList.add("林青霞");
        womanList.add("王祖贤");
        womanList.add("林志玲");

        Stream<String> manStream = manList.stream().limit(3).filter(s -> s.length() == 3);
        Stream<String> womanStream = womanList.stream().filter(s -> s.startsWith("林")).skip(1);

        System.out.println("--------------------");
        Stream<String> stream = Stream.concat(manStream, womanStream);

        stream.map(Actor::new).forEach(s->System.out.println(s.getName()));
        System.out.println("--------------------");
        //得到名字为三个字的流,收据数据到LIST集合中
        Stream<String> listStream = manList.stream().filter(s -> s.length() == 3);
       List<String> namesList = listStream.collect(Collectors.toList());
        for(String name:namesList){
            System.out.println(name);
        }
        System.out.println(namesList);
        System.out.println("--------------------");
        Set<Integer> set = new HashSet<Integer>();
        set.add(10);
        set.add(20);
        set.add(30);
        set.add(40);
        Set<Integer> ageSet = set.stream().filter(s -> s > 25).collect(Collectors.toSet());
        System.out.println(ageSet);
        System.out.println("----------------");
        String[] strArray={"林青霞,30","张曼玉,35","王祖贤,33","柳岩,25"};
        Stream<String> arrayStream = Stream.of(strArray).filter(s -> Integer.parseInt(s.split(",")[1]) > 28);
        System.out.println("------------------------");
        Map<String, Integer> map = arrayStream.collect(Collectors.toMap(s -> s.split(",")[0], s -> Integer.parseInt(s.split(",")[1])));
        Set<String> keySet = map.keySet();
        for (String key:keySet){
            Integer value = map.get(key);
            System.out.println(key+","+value);
        }

    }

}
