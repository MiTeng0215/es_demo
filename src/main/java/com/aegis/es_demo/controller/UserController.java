package com.aegis.es_demo.controller;

import com.aegis.es_demo.bean.RegisterForm;
import com.aegis.es_demo.common.Msg;
import com.aegis.es_demo.common.ResultData;
import com.aegis.es_demo.config.JwtToken;
import com.aegis.es_demo.dao.UserDao;
import com.aegis.es_demo.domin.User;
import com.aegis.es_demo.service.inter.UserService;
import com.aegis.es_demo.utils.CaptchaUtil;
import com.aegis.es_demo.utils.JWTUtil;
import com.aegis.es_demo.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 1、加注解
 * 2、加restcontroller
 * 3、log
 * 4、swagger注释
 *
 *
 */
@Api(tags = "登录接口")
@RestController
public class UserController {
    /**
     * 1、首先先注入数据
     * 2、login功能
     * 3、注册功能
     * 4、退出登录
     * 5、获取验证码
     */
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;
    @Value("${lockTime}")
    private long lockTime;
    @Value("${namespace.token}")
    private String tokenPrefix;
    @Value("${namespace.validKey}")
    private String keyPrefix;
    @Value("${namespace.wrong}")
    private String wrongPrefix;
    @Autowired
    private CaptchaUtil captchaUtil;

    @PostMapping("login")
    public ResultData login(@ApiParam(value = "用户名",required = true) @RequestParam String username,
                            @ApiParam(value = "密码",required = true) @RequestParam String password) {
        /**
         * 1、先寻找该账户根据用户名，如果存在，则寻找该用户，不存在啊返回用户不存在
         *
         */
        User user = userDao.findByName(username);
        User user1 = userDao.findByName(username, password);
        if (user == null) {
            return new ResultData(Msg.AUTH_ERROR, "账号不存在");
        }
        //密码错误
        if (user1 == null) {//密码错误
            if (RedisUtil.hasKey(wrongPrefix+username)){
//                获取错误次数
                Integer wrongTimes = (Integer) RedisUtil.get(wrongPrefix+username);
//                错误次数小于5，次数加一，等于5，锁定账号
                if (wrongTimes < 5){
                    RedisUtil.set(wrongPrefix+username,wrongTimes+1);
                    return new ResultData(Msg.AUTH_ERROR, "密码错误");
                }else {
                    return new ResultData<>(null, 202, "密码错误五次，请半小时后尝试");
                }
            }else {// 没有该key，第一次输错密码
                RedisUtil.set(wrongPrefix+username,1,lockTime);
                return new ResultData(Msg.AUTH_ERROR, "密码错误");
            }
        } else {
            if (RedisUtil.hasKey(wrongPrefix+username)){
                Integer wrongTimes = (Integer)RedisUtil.get(wrongPrefix + username);
                if (wrongTimes >= 5) {//密码没错，但是前面输入的几次全部输错了，依然要锁定账户
                    return new ResultData<>(null, 202, "密码错误5次，账号已锁定，请半小时后尝试");//超过5次
                } else {
                    RedisUtil.delete(wrongPrefix + username);//删除错误记录
                }
            }
            /**
             * 使用redis中的token
             */
            if (RedisUtil.hasKey(tokenPrefix+username)){
                String token = (String) RedisUtil.get(tokenPrefix+username);
                JwtToken jwtToken = new JwtToken(token);
                try {
                    SecurityUtils.getSubject().login(jwtToken);
                } catch (Exception e) {
                    return new ResultData(Msg.AUTH_ERROR, "token无效");
                }

                return new ResultData<>(token);
            }
        }
        /**
         * 如果redis中没有token，则创建一个token
         */
        String token = JWTUtil.sign(username, password);//账号密码正确，获取token
        RedisUtil.set(tokenPrefix + username, token, JWTUtil.refreshTime());
        JwtToken jwtToken = new JwtToken(token);
        try {
            SecurityUtils.getSubject().login(jwtToken);//这时，将
        } catch (Exception e) {
            return new ResultData(Msg.AUTH_ERROR, "token无效");
        }
        return new ResultData<>(token);
    }
    /**
     * 注册功能
     */
    @PostMapping("register")
    public ResultData register(@ApiParam("注册表单") @RequestBody RegisterForm registerForm) {
        User user = userService.register(registerForm);
        return new ResultData(user);
    }

    /**
     *
     * 退出登录
     */
    @ApiOperation(value = "退出登录")
    @PostMapping("exit")
    public ResultData logout(HttpServletRequest request){
        String token = request.getHeader("Authorization");//获取请求的头部，获取token
        User user = JWTUtil.getUsername(token);
        if (user != null) {//如果用户存在，则删除缓存的token
            RedisUtil.delete(tokenPrefix + user.getNickName());
        }
        return new ResultData(Msg.OK);
    }

    @ApiOperation(value = "获取验证码")
    @GetMapping(value = "getImage")
    public void getImage(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("image/jpeg");
        //禁止图像缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires",0);
        captchaUtil.getCaptcha(request, response);
    }

    @GetMapping("setToken")
    public void setToken(HttpServletRequest request, HttpServletResponse response){
        response.setHeader("Authorization", "12346dsfsdf");

    }
}
