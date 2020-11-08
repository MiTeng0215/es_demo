package com.aegis.es_demo.service.impl;

import com.aegis.es_demo.dao.GirlFriendDao;
import com.aegis.es_demo.dao.UserDao;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
public class GirlFriendServiceImpl implements ApplicationContextAware {
    private static HandleUser handleUser;
    private static UserDao userDao;
    private static GirlFriendDao girlFriendDao;
    private static EntityManager entityManager;

    public static void handleCase(Integer state, Integer oldState) {
        if (oldState == null) oldState=0;
        if (state == oldState || oldState==5 && state ==7 || oldState==6 && state ==7) {
            return;
        }
        handleUser.updateUser();
//        switch (state) {
//            case 1:
//                User user = userDao.findById(3).get();
//                user.setPhone("342341");
//                userDao.save(user);
//                System.out.println("accepted+1");
//                break;
//            case 5:
//            case 6:
//                System.out.println("mediated+1 accpeted-1");
//                return;
//            case 7:
//                System.out.println("mediated+1");
//                return;
//            default:
//                System.out.println("default");
//
//
//        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        GirlFriendServiceImpl.userDao = applicationContext.getBean(UserDao.class);
        GirlFriendServiceImpl.girlFriendDao = applicationContext.getBean(GirlFriendDao.class);
        GirlFriendServiceImpl.entityManager = applicationContext.getBean(EntityManager.class);
        GirlFriendServiceImpl.handleUser = applicationContext.getBean(HandleUser.class);
    }
}
