package com.aegis.es_demo.config;


import com.aegis.es_demo.common.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

  @ExceptionHandler({Exception.class})
  public ResultData exception(Exception e) {
    if (e instanceof AuthorizationException) {
      return new ResultData(e instanceof UnauthorizedException ? "无权限" : "登录异常");
    }
    //只返回后台报错，隐藏堆栈信息
    return new ResultData("后台报错");
  }
}
