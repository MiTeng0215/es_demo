package com.aegis.es_demo.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * date 23/01/2019
 *
 * @author SunYiBo
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "obs")
public class ObsProperties {

  private String accessKey;
  private String bucketName;
  private String endPoint;
  private String secretKey;

}
