package com.aegis.es_demo.dao;

import com.aegis.es_demo.domin.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleDao extends JpaRepository<Role,Integer>, JpaSpecificationExecutor<Role> {
     Role findByName(String name);
}
