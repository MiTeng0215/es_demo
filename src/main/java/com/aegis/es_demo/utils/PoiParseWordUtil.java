package com.aegis.es_demo.utils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.core.IXWPFConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;

public class PoiParseWordUtil {
   public static String word2007ToHtml(String filepath) throws IOException {
       // 1) 加载word文档生成 XWPFDocument对象
       InputStream in = new FileInputStream(filepath);
       XWPFDocument document = new XWPFDocument(in);

       // 2) 解析 XHTML配置 (这里设置IURIResolver来设置图片存放的目录)
       File imageFolderFile = new File("/Users/miteng/Desktop/images");
       XHTMLOptions options = XHTMLOptions.create().URIResolver(new FileURIResolver(imageFolderFile));
       options.setExtractor(new FileImageExtractor(imageFolderFile));
       options.URIResolver(uri -> {
           //http://192.168.30.222:8010//uploadFile/....
           System.out.println(uri);
           return uri;
       });
       options.setIgnoreStylesIfUnused(false);
       options.setFragment(true);
       in.close();
       // 3) 将 XWPFDocument转换成XHTML
       ByteArrayOutputStream out = new ByteArrayOutputStream();
       IXWPFConverter<XHTMLOptions> converter = XHTMLConverter.getInstance();
       converter.convert(document,out, options);
       return StringEscapeUtils.unescapeHtml3(out.toString());
   }

    public static void main(String[] args) throws IOException {
        String path = "/Users/miteng/Desktop/调解案例.docx";
        System.out.println(PoiParseWordUtil.word2007ToHtml(path));
    }
}
