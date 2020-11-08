package com.aegis.es_demo.jpaListener;

import com.aegis.es_demo.domin.GirlFriend;
import com.aegis.es_demo.service.impl.GirlFriendServiceImpl;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
public class GirlFriendListener {
    @PrePersist
    public void PrePersist(GirlFriend girlFriend){
        System.out.println("开始保存--"+girlFriend.toString());
        GirlFriendServiceImpl.handleCase(girlFriend.getState(),girlFriend.getOldState());
    }

    @PostPersist
    public void PostPersist(GirlFriend girlFriend){
        System.out.println("结束保存--"+girlFriend.toString());
//        GirlFriendServiceImpl.handleCase(girlFriend.getState(),girlFriend.getOldState());
    }

    @PreUpdate
    public void preUpdate(GirlFriend girlFriend){
        System.out.println("开始更新--"+girlFriend.toString());
//        GirlFriendServiceImpl.handleCase(girlFriend.getState(),girlFriend.getOldState());
    }

    @PostUpdate
    public void PostUpdate(GirlFriend girlFriend){
        System.out.println("结束更新--"+girlFriend.toString());
//        GirlFriendServiceImpl.handleCase(girlFriend.getState(),girlFriend.getOldState());
    }

    @PostLoad
    public void PostLoad(GirlFriend girlFriend){
        System.out.println("查询后--"+girlFriend.toString());
        girlFriend.setOldState(girlFriend.getState());
    }

}
