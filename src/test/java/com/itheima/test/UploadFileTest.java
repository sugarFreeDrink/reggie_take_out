package com.itheima.test;

import org.junit.jupiter.api.Test;

public class UploadFileTest {

    @Test
    public void test1(){
        String originalFileName = "wenji.an.jpg";
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        System.out.println(suffix);
        System.out.println(originalFileName.lastIndexOf("."));
    }
}
