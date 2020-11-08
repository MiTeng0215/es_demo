package com.aegis.es_demo.config;

import com.aegis.es_demo.common.ResultData;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {
    /**
     * 1、检验请求头是否包含Token
     * 2、带有Token，就将token送到shiro的login()方法，与realm的数据进行校验，如果没有Token，说明现在用户处于游客状态
     * 3、如果Token校验失败，这重定向到/unauthorized/**
     * @param request
     * @param response
     * @return
     * @throws Exception
     */

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {//看用户是否携带token
            try {
                executeLogin(request, response);//如果含有token，就执行login
            } catch (Exception e) {
                String msg = e.getMessage();
                Throwable throwable = e.getCause();
                if (throwable instanceof SignatureVerificationException) { //token验证失败
                    msg = "token无效";
                } else if (throwable instanceof TokenExpiredException) { //token过期，刷新token
//                    this.responseExpireToken(request, response, msg);
                    return false;
                }
                this.response401(response, msg, 208);//报错
                return false;
            }
        } else {//没有token，需要重新登录，重新过去token
            this.response401(response, "请先登录", 205);
            return false;
        }
        return false;
    }

    /**
     * 请求是否已经登录（携带Token）
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("Authorization");//请求中是否含有Auth的请求头
        return authorization != null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        String auth = req.getHeader("Authorization");
        JwtToken jwtToken = new JwtToken(auth);
        SecurityUtils.getSubject().login(jwtToken);
        return true;
    }

    private void response401(ServletResponse response, String msg, int code) {
        HttpServletResponse res = (HttpServletResponse) response;
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.setCharacterEncoding("UTF-8");
        res.setContentType("application/json; charset=utf-8");

        try (OutputStream outputStream = res.getOutputStream()) {
            ResultData result = new ResultData<>(null, code, msg);
            outputStream.write(new Gson().toJson(result).getBytes());
        } catch (Exception e) {
            log.error("返回Response信息出现IOException异常:" + e.getMessage());
        }
    }
}
