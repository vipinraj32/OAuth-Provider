package com.oauthprovider.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oauthprovider.DTO.Request;
import com.oauthprovider.DTO.Response;
import com.oauthprovider.entity.Role;
import com.oauthprovider.entity.User;
import com.oauthprovider.exception.ResourseAlreadyExistException;
import com.oauthprovider.repository.RoleRepository;
import com.oauthprovider.repository.UserRepository;
import com.oauthprovider.security.AuthUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthService {

	@Autowired
    private  AuthenticationManager authenticationManager;
	@Autowired
    private  AuthUtil authUtil;
	@Autowired
    private  UserRepository userRepository;
	@Autowired
    private  PasswordEncoder passwordEncoder;
	@Autowired
	private RoleRepository roleRepository;

    public Response login(Request loginRequestDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword())
        );

        User user = (User) authentication.getPrincipal();

        String token = authUtil.generateAccessToken(user);

        return new Response(token, user.getEmail(), user.getAuthorities().toString(), user.getName());
    }
    
	@Transactional
    public User signup(User user) {
    	User userOptional=userRepository.findById(user.getEmail()).orElse(null);
    	if(userOptional!=null) {
    		throw new ResourseAlreadyExistException("Email Already Exist.!!");
    	}
    	Role role=roleRepository.findById(2).orElse(null);
		user.addRole(role);
		return userRepository.save(user);
    }
    public List<User> showAll(){
    	log.info("Service Run showAll");
    	return userRepository.findAll();
    }
    
    
}