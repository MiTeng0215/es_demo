package com.aegis.es_demo.dao;

import com.aegis.es_demo.bean.RegisterForm;
import com.aegis.es_demo.domin.User;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


//需要提供响应的泛型
public interface UserDao extends JpaRepository<User,Integer>, JpaSpecificationExecutor<User> {

    @Query(value = "select * from user",nativeQuery = true)
    List<User> findAll();

    @Query(value = "from User where name = ?1")
    User findByName(String name);

    @Query(value = "from User where name = ?1 and password = ?2")
    User findByName(String name,String password);

    @Query(value = "update User set nick_name=?2 where id=?1",nativeQuery = true)
    @Modifying
    @Transactional(propagation = Propagation.REQUIRED)
    void updateUser(int id,String nick_name);


}
