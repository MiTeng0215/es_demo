package com.aegis.es_demo.springListener;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 逻辑:业务逻辑中发布事件,也就是触发event,监听器监听到发布事件,在监听器中得到该事件,并对该事件做进一步的业务处理
 *
 */
@Component
public class EventListenerDemo implements ApplicationListener<Event> {

    public void onApplicationEvent(Event event) {
        rest();
        drink();
    }
    public void rest() {
        System.out.println("休息1分钟");
    }
    public void drink() {
        System.out.println("喝水");
    }
}
