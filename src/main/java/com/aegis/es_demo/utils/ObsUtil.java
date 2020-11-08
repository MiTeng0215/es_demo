package com.aegis.es_demo.utils;

import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class ObsUtil {
//    //读取yml获取obs配置
//    private static ObsProperties obs;
//    private static String yml = "/application.yml";
//    static {
//        obs = new ObsProperties();
//        Yaml yaml = new Yaml();
//        InputStream inputStream = ObsUtil.class.getResourceAsStream(yml);
//        try {
//            Map<String, Object> map = yaml.load(inputStream);
//            Map<String, Object> map1 = (Map<String, Object>) map.get("obs");
//            obs.setAccessKey(map1.get("accessKey").toString());
//            obs.setBucketName(map1.get("bucketName").toString());
//            obs.setEndPoint(map1.get("endPoint").toString());
//            obs.setSecretKey(map1.get("secretKey").toString());
//        } catch (NumberFormatException e) {
//            log.error(e.getMessage(), e);
//        }
//    }
    private static String bucketName = "tyun-test";
    private static String accessKey = "RQ2KDDUO1YYQT92CJLH1";
    private static String secretKey = "VH6hqIOUj8INp956TVLpVCU0bfOlQpuvuvq4eLA7";
    private static String endPoint = "obs.cn-east-3.myhuaweicloud.com";
    public static Boolean del(String key) {
        DeleteObjectResult res = func(client -> client.deleteObject(bucketName, key));
        return res != null && res.isDeleteMarker();
    }


    private static <T> T func(Function<ObsClient, T> f) {
        ObsClient client = new ObsClient(accessKey, secretKey, endPoint);
        try {
            boolean exist = false;
            try {
                exist = client.headBucket(bucketName);
            } catch (ObsException e) {
                log.error("ObsService headBucket fail: " + e.getErrorCode());
            }
            if (!exist) {
                ObsBucket bucket = new ObsBucket();
                bucket.setBucketName(bucketName);
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

    public static String put(File file, String key) {
        try {
            return put(key, file.getName(), FileUtils.openInputStream(file));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static String put(String s, String name, InputStream ins) {
        if (ins != null) {
            String key = (s != null && !s.isEmpty()) ? s : Md5Utils.getMd5Code(name + System.currentTimeMillis());
            PutObjectResult res = func(client -> {
                String dir = FilenameUtils.getExtension(name) + "/";
                client.putObject(bucketName, dir, new ByteArrayInputStream(new byte[0]));

                PutObjectRequest request = new PutObjectRequest();
                request.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ);
                request.setBucketName(bucketName);
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

    public static String put(MultipartFile file, String key) {
        try {
            return put(key, file.getOriginalFilename(), file.getInputStream());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static String uploadFile(MultipartFile file) {
        return put(file, Encrypt.getUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename()));
    }

    public static List<String> uploadFile(List<MultipartFile> files) {
        return files.stream()
                .map(ObsUtil::uploadFile)
                .collect(Collectors.toList());
    }
}
