package com.aegis.es_demo.service.impl;

import com.aegis.es_demo.properties.ObsProperties;
import com.aegis.es_demo.service.inter.ObsService;
import com.aegis.es_demo.utils.Encrypt;
import com.aegis.es_demo.utils.Md5Utils;
import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * date 19/12/2018
 *
 * @author SunYiBo
 */
@Slf4j
@Service
public class ObsServiceImpl implements ObsService {

  private final ObsProperties obs;

  @Autowired
  public ObsServiceImpl(ObsProperties obs) {
    this.obs = obs;
  }


  @Override
  public Boolean del(String key) {
    DeleteObjectResult res = func(client -> client.deleteObject(obs.getBucketName(), key));
    return res != null && res.isDeleteMarker();
  }


  private <T> T func(Function<ObsClient, T> f) {
    ObsClient client = new ObsClient(obs.getAccessKey(), obs.getSecretKey(), obs.getEndPoint());
    try {
      boolean exist = false;
      try {
        exist = client.headBucket(obs.getBucketName());
      } catch (ObsException e) {
        log.error("ObsService headBucket fail: " + e.getErrorCode());
      }
      if (!exist) {
        ObsBucket bucket = new ObsBucket();
        bucket.setBucketName(obs.getBucketName());
        bucket.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ);
        client.createBucket(bucket);
      }
      return f.apply(client);
    } catch (ObsException e) {
      log.error("Error Message: " + e.getErrorMessage());
      log.error("Error Code:" + e.getErrorCode());
    } finally {
      try {
        client.close();
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }


  @Override
  public String put(File file, String key) {
    try {
      return put(key, file.getName(), FileUtils.openInputStream(file));
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }


  public String put(String s, String name, InputStream ins) {
    if (ins != null) {
      String key = (s != null && !s.isEmpty()) ? s : Md5Utils.getMd5Code(name + System.currentTimeMillis());
      PutObjectResult res = func(client -> {
        String dir = FilenameUtils.getExtension(name) + "/";
        client.putObject(obs.getBucketName(), dir, new ByteArrayInputStream(new byte[0]));

        PutObjectRequest request = new PutObjectRequest();
        request.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ);
        request.setBucketName(obs.getBucketName());
        request.setObjectKey(dir + key);
        request.setInput(ins);

        try {
          return client.putObject(request);
        } catch (ObsException e) {
          log.error("Error Message: " + e.getErrorMessage());
          log.error("Error Code:" + e.getErrorCode());
        } finally {
          try {
            ins.close();
          } catch (IOException e) {
            log.error(e.getMessage(), e);
          }
        }
        return null;
      });
      if (res != null) {
        return res.getObjectUrl().replace("%2F", "/");
      }
    }
    return null;
  }


  @Override
  public String put(MultipartFile file, String key) {
    try {
      return put(key, file.getOriginalFilename(), file.getInputStream());
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }


  @Override
  public String uploadFile(MultipartFile file) {
    return put(file, Encrypt.getUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename()));
  }


  @Override
  public List<String> uploadFile(List<MultipartFile> files) {
    return files.stream()
               .map(this::uploadFile)
               .collect(Collectors.toList());
  }


}
