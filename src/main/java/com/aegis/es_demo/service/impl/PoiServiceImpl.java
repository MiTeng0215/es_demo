package com.aegis.es_demo.service.impl;

import com.aegis.es_demo.service.inter.ObsService;
import com.aegis.es_demo.service.inter.PoiService;
import com.aegis.es_demo.utils.Encrypt;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Objects;

@Slf4j
@Service
public class PoiServiceImpl implements PoiService {
    @Autowired
    private ObsService obsService;


    @Override
    public String getWordTitle(MultipartFile multipartFile) throws IOException {
       return null;
    }

    @Override
    public String wordToHtml(MultipartFile multipartFile) throws IOException, TransformerException, ParserConfigurationException {
        if (Objects.equals(FilenameUtils.getExtension(multipartFile.getOriginalFilename()), "docx")) {
            return word2007ToHtml(multipartFile);
        } else {
            return word2003ToHtml(multipartFile);
        }

    }
    private String word2003ToHtml(MultipartFile multipartFile) throws ParserConfigurationException, TransformerException, IOException {
        InputStream inputStream = multipartFile.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HWPFDocument wordDocument = new HWPFDocument(inputStream);
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .newDocument());
        wordToHtmlConverter.setPicturesManager((content, pictureType, suggestedName, widthInches, heightInches) -> obsService.put(Encrypt.getUUID()+"."+pictureType, suggestedName, new ByteArrayInputStream(content)));
        wordToHtmlConverter.processDocument(wordDocument);
        Document htmlDocument = wordToHtmlConverter.getDocument();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(out);
        //转换html文件
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        out.close();
        //转化数据流，替换特殊字符
        return StringEscapeUtils.unescapeHtml3(out.toString());
    }

    private String word2007ToHtml(MultipartFile multipartFile) throws IOException {
        XWPFDocument docxDocument = new XWPFDocument(multipartFile.getInputStream());
        XHTMLOptions options = XHTMLOptions.create();
        String path = System.getProperty("java.io.tmpdir");//系统临时文件名
        String firstImagePathStr = path + "/" + System.currentTimeMillis();//图片文件夹
        options.setExtractor(new FileImageExtractor(new File(firstImagePathStr)));
        options.URIResolver(new BasicURIResolver(firstImagePathStr));
        ByteArrayOutputStream htmlStream = new ByteArrayOutputStream();
        XHTMLConverter.getInstance().convert(docxDocument, htmlStream, options);
        String htmlStr = htmlStream.toString();
        String middleImageDirStr = "/word/media";
        String imageDirStr = firstImagePathStr + middleImageDirStr;//最终图片文件夹
        File imageDir = new File(imageDirStr);
        String[] imageList = imageDir.list();
        if (imageList != null) {
            for (String s : imageList) {
                String oneImagePathStr = imageDirStr + "/" + s;//图片路径
                File imageFile = new File(oneImagePathStr);
                String imageUrl = obsService.put(imageFile, Encrypt.getUUID()+"."+ FilenameUtils.getExtension(oneImagePathStr));
                htmlStr = htmlStr.replace(oneImagePathStr, imageUrl);
            }
        }
        File firstImagePath = new File(firstImagePathStr);
        FileUtils.deleteDirectory(firstImagePath);
        return StringEscapeUtils.unescapeHtml3(htmlStr);
    }
}
