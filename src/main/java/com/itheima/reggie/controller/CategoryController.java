package com.itheima.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;



    /*
    * 新增分类
    * */
    @PostMapping
    public R<String> save( @RequestBody Category category){
        log.info("category：{}",category);
        categoryService.save(category);
        return  R.success("新增分类成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        //分頁構造器
        Page<Category> pageInfo = new Page<Category>(page,pageSize);

        //条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<Category>();

        //添加排序
        lambdaQueryWrapper.orderByAsc(Category::getSort);

        //jin进行分页查询
        categoryService.page(pageInfo,lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("删除分类,ids为{}",ids);


      //  categoryService.removeById(ids);

        categoryService.remove(ids);
        return  R.success("分类信息删除成功");

    }
    /*根据ID
    * 修改分类信息
    * */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息：{}",category);
        categoryService.updateById(category);

        return R.success("修改分类信息成功");
    }

    @GetMapping("list")
    public R<List<Category>> list(Category category){

        //条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        lambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        //添加排序条件
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(lambdaQueryWrapper);

        return  R.success(list);
    }

}
