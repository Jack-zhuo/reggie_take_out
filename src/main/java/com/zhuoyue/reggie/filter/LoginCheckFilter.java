package com.zhuoyue.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.zhuoyue.reggie.common.BaseContext;
import com.zhuoyue.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();
        log.info("拦截到请求：{}",requestURI);

//         要放行的urls
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"

        };

//        访问的是否为放行页面，如果时，直接放行。
        boolean check = check(urls, requestURI);
        if (check){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
//        检查是否登录,若登录直接放行
        if (request.getSession().getAttribute("employee") != null){
            log.info("用户已登录，用户的id为：{}",request.getSession().getAttribute("employee"));
            BaseContext.setCurrentId((Long) request.getSession().getAttribute("employee"));
            filterChain.doFilter(request,response);
            return;
        }

        //        检查是否登录,若登录直接放行
        if (request.getSession().getAttribute("user") != null){

            BaseContext.setCurrentId((Long) request.getSession().getAttribute("user"));

            filterChain.doFilter(request,response);
            return;
        }
//        如果没有登录，通过输出流的方式向页面响应数据
        log.info("用户未登录！");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

    }

    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean boo = PATH_MATCHER.match(url, requestURI);
            if (boo) return true;
        }
        return false;
    }
}
