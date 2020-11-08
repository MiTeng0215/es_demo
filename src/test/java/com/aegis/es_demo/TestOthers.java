package com.aegis.es_demo;

import com.aegis.es_demo.dao.UserDao;
import com.aegis.es_demo.domin.TestBuilder;
import com.aegis.es_demo.domin.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class TestOthers {
    @Autowired
    private UserDao userDao;
    @Test
    public void hello() {
        PageRequest request = PageRequest.of(0,2);
        Page<User> all = userDao.findAll(request);
//        long total = all.getTotalElements();
//        System.out.println(total);
        new PageImpl<>(all.getContent(),request,all.getTotalElements());
    }

    @Test
    public void test() {
        TestBuilder testBuilder = new TestBuilder();
        testBuilder.setName("mike");
        System.out.println(testBuilder);
    }

    static void go() {
        System.out.println("Go::go()");
    }
    
    @Test
    public void testRunnable() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("");
            }
        }).start();
        new Thread(()->{
            System.out.println("");
        });
        new Thread(Go::go).start();
    }
    @Test
    public void testPrivate() {

    }
}
class Go {
    static void go() {
        System.out.println("Go::go()");
    }
}