package com.itheima.reggie.filter;


import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*检查用户登录过滤
*
* */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginFilterCheck implements Filter {
    //路劲匹配器 ,支持通配符
    public  static final AntPathMatcher PATH_MATCHER= new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        //1.获取本次请求的URI
        String requestURI = httpServletRequest.getRequestURI();
        log.info("拦截到请求：{}",requestURI);


        //定义不需要处理的路径
        String[] urls = new String[]{"/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/login",
                "/common/**",
                "/user/sendMsg",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"

        };
        //2判断本次请求是否需要处理
        boolean check = check(urls, requestURI);
        //3.如果不需要处理直接放行
        if (check){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        //4-1判断登录状态，如果已登录直接放行
        if (httpServletRequest.getSession().getAttribute("employee")!=null){
            log.info("用户已登录，用户id为：{}",httpServletRequest.getSession().getAttribute("employee"));

            Long empId = (Long) httpServletRequest.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            long id = Thread.currentThread().getId();
            log.info("线程id为；{}",id);
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }

        //4-2判断登录状态，如果已登录直接放行
        if (httpServletRequest.getSession().getAttribute("user")!=null){
            log.info("用户已登录，用户id为：{}",httpServletRequest.getSession().getAttribute("user"));

            Long userId = (Long) httpServletRequest.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            long id = Thread.currentThread().getId();
            log.info("线程id为；{}",id);
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        //5.如果未登录则返回未登录结果，通过输出流向客户端页面响应数据
        log.info("用户未登录");
        httpServletResponse.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            return;



    }
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, String.valueOf(requestURI));
            if (match){
                return  true;
            }
        }
        return false;
    }
}
