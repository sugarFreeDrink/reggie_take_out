package com.itheima.stream;

import java.util.ArrayList;

public class StreamDemo03 {
    public static void main(String[] args) {

        ArrayList<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");

        list.stream().map((s -> {

            return s;

        })

        );
    }
}
