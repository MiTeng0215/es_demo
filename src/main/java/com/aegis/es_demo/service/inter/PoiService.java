package com.aegis.es_demo.service.inter;

import org.springframework.web.multipart.MultipartFile;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public interface PoiService {
    /**
     *
     * @param multipartFile 文件
     * @return 获取word的标题
     */
    String getWordTitle(MultipartFile multipartFile) throws IOException;

    /**
     *
     * @param multipartFile 文件
     * @return html字符串,内含图片的话上传到obs中
     */
    String wordToHtml(MultipartFile multipartFile) throws IOException, TransformerException, ParserConfigurationException;
}
