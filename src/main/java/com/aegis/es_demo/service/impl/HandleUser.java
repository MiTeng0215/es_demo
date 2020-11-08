package com.aegis.es_demo.service.impl;

import com.aegis.es_demo.dao.UserDao;
import com.aegis.es_demo.domin.User;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HandleUser implements ApplicationContextAware {
    private UserDao userDao;
//    update User
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateUser(){
        User user = userDao.findById(3).get();
        user.setPhone("sdafvasdfa");
        userDao.save(user);
//       throw  new RuntimeException();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.userDao = applicationContext.getBean(UserDao.class);
    }
}
