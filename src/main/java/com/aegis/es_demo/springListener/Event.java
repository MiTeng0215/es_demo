package com.aegis.es_demo.springListener;


import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 *
 * 跑步10分钟事件
 */
@Getter
@Setter
public class Event extends ApplicationEvent {

    public Event(Object source) {
        super(source);
    }

}
