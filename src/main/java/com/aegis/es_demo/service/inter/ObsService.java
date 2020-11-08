package com.aegis.es_demo.service.inter;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * obs服务
 * date 19/12/2018
 *
 * @author SunYiBo
 */
public interface ObsService {

  /**
   * 删除
   *
   * @param key 键
   * @return 是否成功删除
   * @author SunYiBo
   * @since 19/12/2018
   */
  Boolean del(String key);


  /**
   * 上传
   *
   * @param file 文件
   * @param key  键
   * @return 访问路径
   * @author SunYiBo
   * @since 19/12/2018
   */
  String put(File file, String key);

  /**
   *
   * @param key obs键
   * @param fileName file.getName()
   * @param inputStream 输入流
   * @return 文件obs路径
   */
  String put(String key, String fileName, InputStream inputStream);


  /**
   * 上传
   *
   * @param file 文件
   * @param key  键
   * @return 访问路径
   * @author SunYiBo
   * @since 19/12/2018
   */
  String put(MultipartFile file, String key);


  /**
   * 上传文件
   *
   * @param file 文件
   * @return 访问路径
   */
  String uploadFile(MultipartFile file);


  /**
   * 上传文件
   *
   * @param files 文件
   * @return 访问路径
   */
  List<String> uploadFile(List<MultipartFile> files);


}
