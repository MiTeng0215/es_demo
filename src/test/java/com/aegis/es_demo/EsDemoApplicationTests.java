package com.aegis.es_demo;


import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;


@SpringBootTest
class Demo2ApplicationTests {
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    //    测试api
//   创建索引
    @Test
    public void testCreateIndex() throws IOException {
//        1、创建索引请求
        CreateIndexRequest request = new CreateIndexRequest("teng_index");
//        2、客户端执行请求,请求后获得响应
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);

    }
}