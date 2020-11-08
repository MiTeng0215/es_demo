package com.aegis.es_demo;

import com.aegis.es_demo.bean.RegisterForm;
import com.aegis.es_demo.common.ResultData;
import com.aegis.es_demo.dao.PermissionDao;
import com.aegis.es_demo.dao.RoleDao;
import com.aegis.es_demo.dao.UserDao;
import com.aegis.es_demo.domin.*;
import com.aegis.es_demo.service.inter.UserService;
import com.aegis.es_demo.utils.RedisUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class TestDao {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void testFindAll(){
        List users = userDao.findAll();
//        Gson g = new Gson();
//        String newuser = g.toJson(users);
//        System.out.println(newuser);
        System.out.println(users);
    }
    @Test         //可以是添加，可以是更新，看主键是否存在判断
    public void testSave(){
        User user  = new User();
        user.setId(2);
        user.setName("李天一");
        user.setNickName("李四");
        user.setPassword("qarqwrqw");
        user.setPhone("235453452");
        userDao.save(user);
    }
    @Test
    public void testDelete(){
        User user  = new User();
        user.setId(2);           
        userDao.delete(user);
    }
    @Test
    public  void testFindById(){
        User user = userDao.findById(1).get();
        System.out.println(user);
    }
    @Test
    public void testFindByName(){
        User user = userDao.findByName("李刚","123456");
        User user1 = userDao.findByName("李刚");

        System.out.println(user1);
        System.out.println(user);
    }
    @Test
    public void testUpdateUser(){
        userDao.updateUser(1,"李毛");
    }

    @Test
    @Transactional
    @Rollback(false)//不自动回滚
    public void testManyToMany(){
        User user = userDao.findByName("小沁");
        Role role = roleDao.findById(8).get();
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);

    }


    @Test
    public void testPaged(){
        User user = new User();
        userDao.save(user);
    }

    @Test
    @Transactional
    @Rollback(false)//不自动回滚
    public void testPermisson(){

    }
    @Test
    @Transactional
    @Rollback(false)//不自动回滚
    public void testPermisson2(){


    }

    @Test
    public void testRegister(){
        User user = new User();
        RegisterForm registerForm = new RegisterForm();
        registerForm.setName("sdf");
        registerForm.setNickName("sgsadfas");
        registerForm.setPassword("12312");
        registerForm.setPhone("3112312");
        BeanUtils.copyProperties(registerForm, user);
        User user1 = userService.register(registerForm);
        System.out.println(user1);
    }

    @Test
    public void findByCondition(){
        Specification<User> spec = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                1、获取比较的属性
                Path<Object> name = root.get("name");
//                2、构造查询
                Predicate predicate = cb.equal(name,"吴云朝");
                return predicate;
            }
        };
        User user = userDao.findOne(spec).get();
        System.out.println(user);
    }
    @Test
    public void findByCondition2(){
        Specification<User> spec = ((root,query,cb)->{
//            指定比较类型，name是字符串类型的
            return cb.like(root.get("name"),"%云%");
        });
        List<User> list = userDao.findAll(spec);
        list.forEach(li-> System.out.println(li));
    }

    /**
     * sort查询
     */
    @Test
    public void findByCondition3(){
        Specification<User> spec = ((root,query,cb)->{
//            指定比较类型，name是字符串类型的
            return cb.like(root.get("name"),"%云%");
        });
        Sort sort = new Sort(Sort.Direction.ASC,"id");
        List<User> list = userDao.findAll(spec,sort);
        list.forEach(li-> System.out.println(li));
    }

    /**
     * 分页查询
     */
    @Test
    public void findByCondition4(){
        Specification<User> spec = ((root,query,cb)->{
//            指定比较类型，name是字符串类型的
            Expression expression = root.get("name");
            return cb.like(expression,"%云%");
        });
        Sort sort = new Sort(Sort.Direction.ASC,"id");
        List<User> list = userDao.findAll(spec,sort);
        list.forEach(li-> System.out.println(li));
    }
    /**
     * 分页查询
     */
    @Test
    public void findByCondition5(){
//        pagerequest是pageable的实现类
        PageRequest pageRequest = PageRequest.of(0,3);
        Page<User> page = userDao.findAll(pageRequest);
        System.out.println(page.getContent());
    }


    public void toArray(String firstname, String secondname){

        String name = "firstname";
        String dfsd = "firstname";

    }
    @Test
    public void sss() {
        String a = "sdc";
        String b = "fsdf";
        toArray(a,b);
    }
    @Test
    public void sda() {
        List<User> list = userDao.findAll();
        System.out.println(list);
    }

    @Test
    @Transactional
    @Rollback(false)//不自动回滚
    public void testRolePermission() {
       Role role = roleDao.findByName("好朋友");
       Permission permission = permissionDao.findByCode("call_anytime");
       List<Permission> list = new ArrayList<>();
       list.add(permission);
       role.setPermissions(list);
    }

    @Test
    public void getRole() {
        User user = userDao.findByName("小沁");
        List<Role> list = user.getRoles();

    }

}
