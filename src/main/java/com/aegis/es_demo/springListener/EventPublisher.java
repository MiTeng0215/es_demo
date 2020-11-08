package com.aegis.es_demo.springListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    @Autowired
    private ApplicationContext applicationContext;

    // 事件发布方法
    public void pushListener() {
        applicationContext.publishEvent(new Event(this));
    }
}
