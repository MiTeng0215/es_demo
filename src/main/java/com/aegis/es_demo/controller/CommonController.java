package com.aegis.es_demo.controller;

import com.aegis.es_demo.common.Data;
import com.aegis.es_demo.common.Pager;
import com.aegis.es_demo.common.ResultData;
import com.aegis.es_demo.dao.UserDao;
import com.aegis.es_demo.domin.GirlFriend;
import com.aegis.es_demo.domin.User;
import com.aegis.es_demo.service.inter.ObsService;
import com.aegis.es_demo.vo.UserVO;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@Slf4j
@Validated
@Api(tags = "通用控制器")
@RequestMapping("/api/user")
public class CommonController {
    @Autowired
    UserDao userDao;

    @RequiresRoles(value = "好朋友")
    @ApiOperation(value = "查询所有数据", httpMethod = "GET")
    @GetMapping
    public ResultData getAll() {
        List<User> users = userDao.findAll();
        users.forEach(user -> user.setRoles(null));
        return new ResultData(users);
    }

    @ApiOperation(value = "根据id查询某个用户", httpMethod = "GET")
    @GetMapping("/{id}")
    public ResultData getById(@PathVariable Integer id) {
        Optional<User> user = userDao.findById(id);
        return new ResultData(user);
    }

    @ApiOperation(value = "根据用户姓名查询用户",httpMethod = "GET")
    @GetMapping("/name")
    public ResultData getByName(@ApiParam(value = "姓名") @RequestParam String name){
         User user = userDao.findByName(name);
         return new ResultData(user);
    }
    @ApiOperation(value = "保存用户",httpMethod = "POST")
    @PostMapping
    public ResultData saveUser(
            @ApiParam(value ="姓名" )@RequestParam String name,
            @ApiParam(value = "昵称")@RequestParam String nick_name,
            @ApiParam(value = "密码")@RequestParam String password,
            @ApiParam(value = "手机号码")@RequestParam String num){
        User user = new User();
        user.setName(name);
        user.setNickName(nick_name);
        user.setPassword(password);
        user.setPhone(num);
        userDao.save(user);
        return new ResultData("post:"+user.getName());
    }
    @ApiOperation(value = "更新用户",httpMethod = "PUT")
    @PutMapping
    public  ResultData updateUser(@PathVariable Integer id,String name){
        User user = (User) userDao.findAllById(Collections.singleton(id));
        user.setName(name);
        return new ResultData("update"+user.getId());
    }
    @ApiOperation(value = "删除用户",httpMethod = "DELETE")
    @DeleteMapping
    public ResultData deleteUser(@RequestParam Integer id){
        User user = new User();
        user.setId(id);
        userDao.delete(user);
        return new ResultData("delete"+user.getId());
    }

    @ApiOperation(value = "查询所有-分页",httpMethod = "GET")
    @GetMapping("/users/{page}")
    public Data findAll(@PathVariable int page){
        PageRequest pageRequest = PageRequest.of(page,4);
        Page<User> userPage = userDao.findAll(pageRequest);
        List<User> users = userPage.getContent();
        Pager pager = new Pager();
        pager.setTotal((int) userPage.getTotalElements());
        pager.setPage(page);
        pager.setPageSize(4);
        System.out.println(users);
        return new Data(users,pager);
    }
    @Autowired
    ObsService obsService;
    @ApiOperation(value = "文件上传",httpMethod = "POST")
    @PostMapping(value = "file_upload")
    public ResultData uploadFile(@RequestBody MultipartFile file){
        String data = obsService.uploadFile(file);
        return new ResultData<>(data);
    }

}