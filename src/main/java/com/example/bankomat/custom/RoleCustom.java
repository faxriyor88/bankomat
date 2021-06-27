package com.example.bankomat.custom;

import com.example.bankomat.entity.Role;
import org.springframework.data.rest.core.config.Projection;

@Projection(types = Role.class)
public interface RoleCustom {
}
