package com.oauthprovider.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.oauthprovider.repository.UserRepository;
@Service
public class CusUserDetails implements UserDetailsService {
	
	@Autowired
	 private  UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String useremail) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		  return userRepository.findById(useremail).orElseThrow();
	}

}
