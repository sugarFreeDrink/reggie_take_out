package com.itheima.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/*菜品管理
* */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(String.valueOf(dishDto));
        dishService.saveWithFlavor(dishDto);

        //清理某个分类下面菜品缓存数据
        String key ="dish_"+dishDto.getCategoryId()+"_1";
        stringRedisTemplate.delete(key);

        return R.success("新增菜品成功");
    }
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
            dishService.updateWithFlavor(dishDto);

            //清理所有菜品的缓存
      /*  Set<String> keys = stringRedisTemplate.keys("dish_*");
        stringRedisTemplate.delete(keys);*/

        //清理某个分类下面菜品缓存数据
        String key ="dish_"+dishDto.getCategoryId()+"_1";
        stringRedisTemplate.delete(key);

        return R.success("新增菜品成功");
    }

  /*  @GetMapping("/list")
    public R<List<Dish>> list( Dish dish){

        LambdaQueryWrapper<Dish> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId()   );
        lambdaQueryWrapper.eq(Dish::getStatus,1);
        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);


        List<Dish> list = dishService.list(lambdaQueryWrapper);
        return R.success(list);
    }*/

    @GetMapping("/list")
    public R<List<DishDto>> list( Dish dish){

        List<DishDto> dishDtoList =null;

        String key = "dish_" +dish.getCategoryId()+"_"+dish.getStatus(); //dish_id_1
        //从redis中获取缓存数据
        String s = stringRedisTemplate.opsForValue().get(key);
        Object o=s;
        dishDtoList = (List<DishDto>) o;

        //如果存在 直接返回，无需查询数据
        if (dishDtoList !=null){
            return R.success(dishDtoList);
        }


        //不存在需要查询数据

        LambdaQueryWrapper<Dish> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId()   );
        lambdaQueryWrapper.eq(Dish::getStatus,1);
        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);


        List<Dish> list = dishService.list(lambdaQueryWrapper);

        dishDtoList=list.stream().map((item)->{

            DishDto dishDto =new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category!=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            };
                //当前菜品的ID
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper =new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            // SQL: select * from dish_flavor where dish_id=?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
           dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        //查询到的数据缓存到redis
        stringRedisTemplate.opsForValue().set(key, String.valueOf(dishDtoList),10, TimeUnit.MINUTES);

        return R.success(dishDtoList);
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
            //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<Dish>();

        //过滤条件
        lambdaQueryWrapper.like(name!=null,Dish::getName,name);
        //排序条件
        lambdaQueryWrapper.orderByAsc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo,lambdaQueryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list1 = records.stream().map(item ->
                {
                    //对象拷贝
                    DishDto dishDto = new DishDto();
                    BeanUtils.copyProperties(item, dishDto);

                    //分类ID
                    Long categoryId = item.getCategoryId();
                    //根据ID查对象
                    Category category = categoryService.getById(categoryId);
                    if (category !=null) {
                        String categoryName = category.getName();
                        dishDto.setCategoryName(String.valueOf(categoryName));
                    }
                    return dishDto;
                }

        ).collect(Collectors.toList());
        List<DishDto> list =list1;


        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }
}
