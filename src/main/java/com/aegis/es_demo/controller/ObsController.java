package com.aegis.es_demo.controller;

import com.aegis.es_demo.service.inter.ObsService;
import com.aegis.es_demo.service.inter.PoiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

@RestController
@Api(tags = "obs")
@RequestMapping("/api/obs")
public class ObsController {
    @Autowired
    private PoiService poiService;
    @Autowired
    private ObsService obsService;

    @ApiOperation(value = "上传word")
    @PostMapping
    public String uploadFile(@RequestBody MultipartFile file ) throws IOException, TransformerException, ParserConfigurationException {
        String downPath = obsService.uploadFile(file);
        String htmlStr = poiService.wordToHtml(file);
        return downPath+"---------"+htmlStr;
    }
}
