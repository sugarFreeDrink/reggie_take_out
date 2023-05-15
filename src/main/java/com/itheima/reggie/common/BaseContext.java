package com.itheima.reggie.common;

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();

    public static void setCurrentId(Long id){
        /*设置值
        * */
        threadLocal.set(id);
    }
    public static Long   getCurrentId(){

        /*获取值
        * */
      return   threadLocal.get();

    }

}
