package com.example.bankomat.repository;

import com.example.bankomat.custom.RoleCustom;
import com.example.bankomat.entity.Role;
import com.example.bankomat.entity.enums.Rolename;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "roles",excerptProjection = RoleCustom.class)
public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByRolename(Rolename rolename);
}
