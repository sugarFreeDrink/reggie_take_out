package com.itheima.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Lang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    /*
    * 员工登录
    * */
    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee, HttpServletRequest request ){

        /*md5加密
        * */
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        /*
        * 查询数据
        * */
        LambdaQueryWrapper<Employee> querryWrapper = new LambdaQueryWrapper<>();
        querryWrapper.eq(Employee::getUsername,employee.getUsername()   );
        Employee emp = employeeService.getOne(querryWrapper);

        // 如果没有查询到返回失败结果
        if (emp == null){
            return R.error("登陆失败");
        }
        // 密码比对 如果不一致 返回失败结果
        if (!emp.getPassword().equals(password))
        {
            return R.error("密码错误");
        }
        //查看状态 是否被禁用
        if (emp.getStatus() == 0){
            return  R.error("账号禁用了");
        }
        //dengl登录成功，将员工ID存入 session并返回登陆成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return  R.success(emp);


    }
    /*
    * 退出方法
    * */
    @PostMapping("/logout")
    public R<String> logout( HttpServletRequest request ) {
        //清除个人数据
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
    /*新增员工
    * */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息：{}",employee.toString());
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
 /*
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        long empId = (long) request.getSession().getAttribute("employee");

        employee.setCreateUser (empId);
        employee.setUpdateUser(empId);
*/
        employeeService.save(employee);

        return  R.success("新增员工成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name ){
        log.info("page={},pageSize={},name={}",page,pageSize,name);

        //构造分页器
        Page pageInfo = new Page(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<Employee>();

        //添加过滤条件
        lambdaQueryWrapper.like(StringUtils.isNotBlank(name),Employee::getName,name);
        //添加排序条件
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询

        Page page1 = employeeService.page(pageInfo, lambdaQueryWrapper);
        return R.success(pageInfo);
    }
    @PutMapping
    public R<String> update(HttpServletRequest httpServletRequest,@RequestBody Employee employee){
        log.info(employee.toString());

        long id = Thread.currentThread().getId();
        log.info("线程id为；{}",id);

    /*    Long emp = (Long) httpServletRequest.getSession().getAttribute("employee");
        employee.setUpdateUser(emp);
        employee.setUpdateTime(LocalDateTime.now());
        */

        employeeService.updateById(employee);

        return R.success("员工信息修改成功");
    }
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息...");
        Employee emp = employeeService.getById(id);
        return  R.success(emp);
    }


}
