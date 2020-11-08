package com.aegis.es_demo.dao;

import com.aegis.es_demo.domin.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PermissionDao extends JpaRepository<Permission,Integer>, JpaSpecificationExecutor<Permission> {
    Permission findByCode(String code);
}
