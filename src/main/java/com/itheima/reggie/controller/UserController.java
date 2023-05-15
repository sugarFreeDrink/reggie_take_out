package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.utils.SMSUtils;
import com.itheima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.undertow.UndertowWebServer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpSession;
import java.util.Map;


@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){

        //获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            //生成随机四位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);

            //调用阿里云API发送短信服务
         //   SMSUtils.sendMessage("个人交流学习",phone,"SMS_276296391",code);

            //保存生成的验证码到Session
            session.setAttribute(phone,code);
            return  R.success("手机验证码短信发送成功");

        }
        return  R.error("手机验证码短信发送失败");

    }

    /*
    * 移动端用户登录
    * */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){

        log.info(map.toString());
        //获取手机号
        String phone = map.get("phone").toString();


        //获取验证码
        String code = map.get("code").toString();

        //从session中获取保存的验证码进行比对
        Object codeInSession = session.getAttribute(phone);
        if (codeInSession !=null && codeInSession.equals(code)){
            //比对成功则登录成功
            LambdaQueryWrapper<User> lambdaQueryWrapper =new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(lambdaQueryWrapper);

            if (user == null){
                //手机号是否为新用户，自动注册
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return  R.success(user);
        }


        return  R.error("登陆失败");

    }
}
