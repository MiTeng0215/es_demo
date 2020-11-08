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
@Table(name = "role")
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true,nullable = false)
    private String name;
    @Column(unique = true,nullable = false)
    private String descrption;


    @ManyToMany
    @JoinTable(name = "role_permission", joinColumns = {@JoinColumn(name = "role_id")},
    inverseJoinColumns = {@JoinColumn(name = "permission_id")})
    private List<Permission> permissions;
}
