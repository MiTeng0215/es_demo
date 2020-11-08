package com.aegis.es_demo.service.impl;

import com.aegis.es_demo.dao.GirlFriendDao;
import com.aegis.es_demo.domin.GirlFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MethodService {
    @Autowired
    GirlFriendDao girlFriendDao;
    public void method1() {
        GirlFriend girlFriend = girlFriendDao.findById(2).get();
        girlFriend.setState(1);
        girlFriendDao.save(girlFriend);
//        GirlFriend girlFriend = new GirlFriend();
//        girlFriend.setName("小ddd");
//        girlFriend.setState(7);
//        girlFriendDao.save(girlFriend);
    }
}
