package com.aegis.es_demo.utils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
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
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
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

@Component
public class DemoUtil {

    public static String word2007(MultipartFile file) throws IOException {
        String name = FilenameUtils.getExtension(file.getOriginalFilename());
        XWPFDocument docxDocument = new XWPFDocument(file.getInputStream());
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
                String imageUrl = ObsUtil.put(imageFile, Encrypt.getUUID()+"."+FilenameUtils.getExtension(oneImagePathStr));
                htmlStr = htmlStr.replace(oneImagePathStr, imageUrl);
            }
        }
        File firstImagePath = new File(firstImagePathStr);
        FileUtils.deleteDirectory(firstImagePath);
        return StringEscapeUtils.unescapeHtml3(htmlStr);
    }
    private static MultipartFile getMulFileByPath(String picPath) {
        FileItem fileItem = createFileItem(picPath);
        return new CommonsMultipartFile(fileItem);
    }
    private static FileItem createFileItem(String filePath)
    {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "textField";
        int num = filePath.lastIndexOf(".");
        String extFile = filePath.substring(num);
        FileItem item = factory.createItem(textFieldName, "text/plain", true,
                "MyFileName" + extFile);
        File newfile = new File(filePath);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try
        {
            FileInputStream fis = new FileInputStream(newfile);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192))
                    != -1)
            {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return item;
    }
    public static String word2003(MultipartFile file) throws IOException, ParserConfigurationException, TransformerException {
        String filePath = "/Users/miteng/Desktop";
        String targetFileName = filePath +"/"+ "temp.html";
        File target = new File(targetFileName);
        target.getParentFile().mkdirs();
        //将上传的文件传入Document转换
        HWPFDocument wordDocument = new HWPFDocument(file.getInputStream());
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(document);
        // word文档转Html文档
        wordToHtmlConverter.processDocument(wordDocument);
        Document htmlDocument = wordToHtmlConverter.getDocument();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(new File(targetFileName));
        //将读取到的图片上传并添加链接地址
        wordToHtmlConverter.setPicturesManager((imageStream, pictureType, name, width, height) -> {
            String imageUrl = null;
            try {
                imageUrl = ObsUtil.put(Encrypt.getUUID()+"."+pictureType, name, new ByteArrayInputStream(imageStream));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return imageUrl;
        });
        //生成html文件
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        // 读取并过滤文件格式
        String htmlContent = splitContext(targetFileName);
        // 删除生成的html文件
        File files = new File(targetFileName);
        files.delete();
        return htmlContent;
    }
    public static String splitContext(String filePath) {
        File file = new File(filePath);
        BufferedReader reader = null;
        try {
            String tempString;
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
            reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
            }
            reader.close();
            // 将文件中的双引号替换为单引号
            String content = sb.toString().replaceAll("\"","\'");
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return "";
    }


    public static String word2003V2(FileInputStream inputStream) throws IOException, ParserConfigurationException, TransformerException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HWPFDocument wordDocument = new HWPFDocument(inputStream);
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .newDocument());
        wordToHtmlConverter.setPicturesManager((content, pictureType, suggestedName, widthInches, heightInches) -> {
            return ObsUtil.put(Encrypt.getUUID()+"."+pictureType, suggestedName, new ByteArrayInputStream(content));
        });
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

    public static void main(String[] args) throws IOException, TransformerException, ParserConfigurationException {
        String path = "/Users/miteng/Desktop/调解案例doc.doc";
        FileInputStream fileInputStream = new FileInputStream(path);
        String s = word2003V2(fileInputStream);
        System.out.println(s);
//        String wordPath = ObsUtil.uploadFile(file);
//        System.out.println(wordPath);
//        String html = StringEscapeUtils.unescapeHtml3(DemoUtil.wDord2007(file));
//        System.out.println(html);
//        File file = new File(path);
//        System.out.println(ObsUtil.put(file, Encrypt.getUUID()+"."+FilenameUtils.getExtension(path)));
    }
}
