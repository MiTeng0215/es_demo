package com.aegis.es_demo.domin;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Setter
@Getter
@Entity
@ToString
@Table(name = "user")//对应数据中的表
public class User implements Serializable {
    @Id//id声明主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//主键生成策略
    private Integer id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String password;
    @Column(name = "nick_name", nullable = false)
    private String nickName;
    @Column(name = "phone")
    private String phone;

    /**
     * 一个用户可以拥有多个角色，一个角色被多个用户拥有
     *
     */
    @ManyToMany
    @JoinTable(name = "user_role",joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> roles;


//    public void setRoles(List<Role> roles) {
//        this.roles = roles;
//    }
}
