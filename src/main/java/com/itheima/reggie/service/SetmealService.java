package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import org.springframework.stereotype.Service;

import java.util.List;


public interface SetmealService extends IService<Setmeal> {

    /*新增套餐，同时保持菜品和套餐的关联关系
    * */

    public  void saveWithDish(SetmealDto setmealDto);

    public void removeWithDish(List<Long> ids);

}
