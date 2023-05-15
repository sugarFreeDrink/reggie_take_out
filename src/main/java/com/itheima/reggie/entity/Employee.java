package com.itheima.reggie.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.annotation.FieldFill.INSERT;
import static com.baomidou.mybatisplus.annotation.FieldFill.INSERT_UPDATE;
import static com.baomidou.mybatisplus.annotation.IdType.ASSIGN_ID;

/*
* 员工实体类
*
* */
@Data
@TableName("employee")
public class Employee implements Serializable {
    private  static final long serialVersionUID=1L;
    private String username;
    private String name;
    private  String password;
    private String phone;
    private String sex;
    private String idNumber;
    private  Integer status;

    @TableField(fill = INSERT)
    private LocalDateTime createTime;

    @TableField(fill = INSERT_UPDATE)
    private LocalDateTime updateTime;

   /* @TableId(type = IdType.ASSIGN_ID)*/
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    private  Long createUser;

    @TableField(fill = INSERT_UPDATE)
    private long updateUser;


}
