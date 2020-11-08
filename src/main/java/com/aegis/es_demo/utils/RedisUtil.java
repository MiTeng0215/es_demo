package com.aegis.es_demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisUtil {
    private static RedisTemplate<String, Object> redisTemplate;
//    注入我们自己写的redisTemplate
    @Autowired
    private void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisSerializer keySerializer = new StringRedisSerializer();
        RedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        RedisUtil.redisTemplate = redisTemplate;
    }
//    设置过期时间，返回ture，成功设置
    public static boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /*************String************/
    //判断key是否存在
    public static boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
    //获取缓存
    public static Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }
    //放入缓存
    public static boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
    //放入缓存并设置过期时间
    public static boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
    //删除缓存
    public static void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }
    /**************redis的map操作**************/
//    获取map对象的所有key值
    public static Set hkeys (String key) {
        return redisTemplate.opsForHash().keys(key);
    }
//    获取某个key对象的map
    public static Object hget(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    public static boolean hset(String key, String field, Object value) {
        try {
            redisTemplate.opsForHash().put(key, field, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public static boolean hset(String key, String field, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, field, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
    public static RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }
}
