package com.aegis.es_demo.utils;

import com.aegis.es_demo.dao.UserDao;
import com.aegis.es_demo.domin.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Component
public class JWTUtil {

    private static long time;
    private static long refreshTime;
    private static String secret;
    private static UserDao userDao;

    @Autowired
    private void setUserDao(UserDao userDao){
        JWTUtil.userDao = userDao;
    }

    //@Value注解可以直接读取yaml中的配置文件
    @Value("${jwt.expiration}")//设置过期时间
    private void setTime(long time) {
        JWTUtil.time = time;
    }

    @Value("${jwt.refreshExpiration}")//刷新过期时间
    private void setRefreshTime(long refreshTime) {
        JWTUtil.refreshTime = refreshTime;
    }

    @Value("${jwt.secret}")
    private void setSecret(String secret) {
        JWTUtil.secret = secret;
    }

    public static String sign(String username, String password) {
        try {
            return JWT.create()
                    .withClaim("username", username)
                    .withExpiresAt(new Date(System.currentTimeMillis() + time * 1000))
                    .sign(Algorithm.HMAC512(password));//使用账号密码作为加密的secret，修改密码时原token失效
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static Integer verify(String token,String username, String password) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(password);//加密算法
            JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
            DecodedJWT jwt = verifier.verify(token);
            return 0;
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
            return -1;
        } catch (TokenExpiredException e) {
            log.error(e.getMessage());
            return 1;
        } catch (SignatureVerificationException e) {
            log.error(e.getMessage());
            return 2;
        }
    }

    public static User getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            String username = jwt.getClaim("username").asString();
//            String username = "李刚";
            return userDao.findByName(username);

        } catch (JWTDecodeException e) {
            return null;
        }
    }
    //优化失效时间，控制在夜晚12点
    public static long refreshTime() {
        LocalDateTime login = LocalDateTime.now();
        int hours = (int)refreshTime/(60*60);
        login = login.plus(hours, ChronoUnit.HOURS);
        if (login.getHour() < 12) {
            login = LocalDateTime.now();
        }
        login = login.withHour(23);
        login = login.withMinute(59);
        login = login.withSecond(59);
        Duration duration = Duration.between(LocalDateTime.now(), login);
        return duration.getSeconds();

    }
}
