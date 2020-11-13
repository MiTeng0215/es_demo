package com.aegis.es_demo.utils;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class AsposeUtil {
    public static String docPdf(MultipartFile multipartFile)   {

        if (!isWordLicense()) {
            return null;
        }
        String pdfUrl = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document doc = new Document(multipartFile.getInputStream()); // Address是将要被转化的word文档
            doc.save(outputStream, SaveFormat.PDF);// 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF,
            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            pdfUrl = ObsUtil.put(Encrypt.getUUID()+".pdf", multipartFile.getOriginalFilename(), inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // EPUB, XPS, SWF 相互转换
        return pdfUrl;
    }

    /**
     * @Description: 验证aspose.word组件是否授权：无授权的文件有水印和试用标记
     */
    public static boolean isWordLicense() {
        boolean result = false;
        try {
            String licensexml = "<License>\n" + "<Data>\n" + "<Products>\n"
                    + "<Product>Aspose.Total for Java</Product>\n" + "<Product>Aspose.Words for Java</Product>\n"
                    + "</Products>\n" + "<EditionType>Enterprise</EditionType>\n"
                    + "<SubscriptionExpiry>20991231</SubscriptionExpiry>\n"
                    + "<LicenseExpiry>20991231</LicenseExpiry>\n"
                    + "<SerialNumber>23dcc79f-44ec-4a23-be3a-03c1632404e9</SerialNumber>\n" + "</Data>\n"
                    + "<Signature>\n"
                    + "sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=\n"
                    + "</Signature>\n" + "</License>";
            InputStream inputStream = new ByteArrayInputStream(licensexml.getBytes());
            com.aspose.words.License license = new com.aspose.words.License();
            license.setLicense(inputStream);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
