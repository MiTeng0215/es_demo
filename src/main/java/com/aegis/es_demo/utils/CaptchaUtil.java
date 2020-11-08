package com.aegis.es_demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

//图片验证码
@Slf4j
@Component
public class CaptchaUtil {

  private String keyPrefix;
  @Value("${namespace.validKey}")
  private void setKeyPrefix(String keyPrefix) {
    this.keyPrefix = keyPrefix;
  }
  private String randomString = "0123456789";
  private int width = 95;// 图片宽
  private int height = 40;// 图片高
  private int lineNum = 40;// 干扰线数量
  private int stringNum = 4;// 验证码字符个数
  private Random random = new Random();

  private Font getFont() {
    return new Font("Consoles", Font.CENTER_BASELINE, 22);
  }

  private Color getRandomColor(int fc, int bc) {
    if (fc > 255) {
      fc = 255;
    }
    if (bc > 255) {
      bc = 255;
    }
    int r = fc + random.nextInt(bc - fc - 16);
    int g = fc + random.nextInt(bc - fc - 14);
    int b = fc + random.nextInt(bc - fc - 18);
    return new Color(r, g, b);
  }

  private String getRandomString(int num) {
    return String.valueOf(randomString.charAt(num));
  }

  private String drawString(Graphics graphics, String s, int i) {
    graphics.setFont(getFont());
    graphics.setColor(new Color(random.nextInt(101), random.nextInt(111), random.nextInt(121)));
    String rand = getRandomString(random.nextInt(randomString.length()));
    s += rand;
    graphics.translate(random.nextInt(5), random.nextInt(3));
    graphics.drawString(rand, 13 * (i+1), 24);
    return s;
  }

  //绘制干扰线
  private void drawLine(Graphics graphics) {
    int x = random.nextInt(width);
    int y = random.nextInt(height);
    int xl = random.nextInt(13);
    int yl = random.nextInt(15);
    graphics.drawLine(x, y, x + xl, y + yl);
  }

  public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
    Graphics graphics = image.getGraphics();
    graphics.fillRect(0, 0, width, height);// 图片大小
    graphics.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 18));
    graphics.setColor(getRandomColor(110, 133));
    for (int i = 0; i <= lineNum ; i++) {
      drawLine(graphics);
    }
    String s = "";// 产生的随机字符
    for (int i = 0; i < stringNum; i++) {
      s = drawString(graphics, s, i);
    }
    String uuid = UUID.randomUUID().toString().replace("-", "");
    RedisUtil.set(keyPrefix + uuid, s, 120);
    response.setHeader("Authorization", uuid);
    graphics.dispose();
    try {
      ImageIO.write(image, "JPEG", response.getOutputStream());
    } catch (IOException e) {
      log.error("图片生成失败");
    }
  }
}
