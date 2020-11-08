package com.aegis.es_demo.service.impl;

import com.aegis.es_demo.bean.RegisterForm;
import com.aegis.es_demo.dao.UserDao;
import com.aegis.es_demo.domin.User;
import com.aegis.es_demo.service.inter.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public User register(RegisterForm registerForm) {
        User user = new User();
        BeanUtils.copyProperties(registerForm, user);
        return userDao.save(user);
    }
}
