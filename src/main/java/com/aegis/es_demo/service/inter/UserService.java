package com.aegis.es_demo.service.inter;

import com.aegis.es_demo.bean.RegisterForm;
import com.aegis.es_demo.domin.User;
import org.springframework.stereotype.Service;


public interface UserService  {
    User register(RegisterForm registerForm);
}
