package com.aegis.es_demo.utils;

import java.util.UUID;

/**
 * description 编码工具类
 * date 21/11/2018
 *
 * @author SunYiBo
 */
public class Encrypt {

  /**
   * @return 32位UUID
   * @author SunYiBo
   * @since 22/11/2018
   */
  public static String getUUID() {
    return UUID.randomUUID().toString().replace("-", "").toLowerCase();
  }

//  public static void main(String[] args) {
//    System.out.println(getUUID());
//  }

}

