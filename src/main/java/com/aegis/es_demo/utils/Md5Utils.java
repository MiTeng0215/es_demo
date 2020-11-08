package com.aegis.es_demo.utils;

import org.springframework.util.DigestUtils;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author zhouyi
 * 2015-12-22 下午2:44:55
 */
public class Md5Utils {

  public static String getMd5Code(String s) {
    return DigestUtils.md5DigestAsHex(s.getBytes(UTF_8));
  }


}
