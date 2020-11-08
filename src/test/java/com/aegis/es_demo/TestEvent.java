package com.aegis.es_demo;

import com.aegis.es_demo.service.inter.ObsService;
import com.aegis.es_demo.springListener.EventPublisher;
import com.aegis.es_demo.utils.Encrypt;
import com.obs.services.ObsClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TestEvent {
    @Autowired EventPublisher eventPublisher;


    /**
     * 做一个测试,每跑步10分钟休息1分钟,然后喝点水
     * 事件:跑步10分钟
     * 监听器:监听以上事件,并处理休息,喝水的逻辑
     */
    @Test
    public void testEvent() {
        for (int i=0;i<100;i++) {
            if (i==0) continue;
            if (i%10 == 0) {
                System.out.println("跑了10分钟了");
                this.eventPublisher.pushListener();
            }
        }
    }
    @Autowired
    private ObsService obsService;

    @Test
    public void obs() {
        String path = "/Users/miteng/Desktop/image1.png";
        File file = new File(path);
        obsService.put(file, Encrypt.getUUID());
    }

    @Test
    public void testClienet() {
        ObsClient client = new ObsClient("TMOP9L7MK8HIRC4TJYPU","2u86afHEqNTrnTWJ2LuZwCZ8y0G2TnRNlN3QgcyU","obs.cn-north-1.myhuaweicloud.com");
    }
}
