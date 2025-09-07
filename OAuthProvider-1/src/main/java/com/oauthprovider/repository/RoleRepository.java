package com.oauthprovider.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oauthprovider.entity.Role;

public interface RoleRepository  extends JpaRepository<Role,Integer>{

}
