package com.oauthprovider.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oauthprovider.DTO.Request;
import com.oauthprovider.DTO.Response;
import com.oauthprovider.entity.AuthProviderType;
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
    
	
//    public User signup(User user) {
//    	User userOptional=userRepository.findById(user.getEmail()).orElse(null);
//    	if(userOptional!=null) {
//    		throw new ResourseAlreadyExistException("Email Already Exist.!!");
//    	}
//    	Role role=roleRepository.findById(2).orElse(null);
//		user.addRole(role);
//		return userRepository.save(user);
//    }
    public List<User> showAll(){
    	log.info("Service Run showAll");
    	return userRepository.findAll();
    }
    
    public User signUpInternal(Request signupRequestDto, AuthProviderType authProviderType,String name, String providerId) {
        User user = userRepository.findById(signupRequestDto.getEmail()).orElse(null);

        if(user != null) throw new IllegalArgumentException("User already exists");
         Role role=roleRepository.findById(2).orElse(null);
        user = User.builder()
                .email(signupRequestDto.getEmail())
                .name(name)
                .providerId(providerId)
                .providerType(authProviderType)
                .roles(Set.of(role)) // Role.PATIENT
                .build();

        if(authProviderType == AuthProviderType.EMAIL) {
            user.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
        }
//        user.addRole(role);
        user = userRepository.save(user);

       return user;
    }
    
    @Transactional
    public User signup(User signupRequestDto) {
    	Request request=Request.builder()
    			.email(signupRequestDto.getEmail())
    			.password(signupRequestDto.getPassword())
    			.build();
        User user = signUpInternal(request, AuthProviderType.EMAIL,signupRequestDto.getName(), null);
        return user;
    }
    
    @Transactional
    public ResponseEntity<Response> handleOAuth2LoginRequest(OAuth2User oAuth2User, String registrationId) {
        AuthProviderType providerType = authUtil.getProviderTypeFromRegistrationId(registrationId);
        String providerId = authUtil.determineProviderIdFromOAuth2User(oAuth2User, registrationId);

        User user = userRepository.findByProviderIdAndProviderType(providerId, providerType).orElse(null);
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        log.info(name);

        User emailUser = userRepository.findByEmail(email).orElse(null);

        if(user == null && emailUser == null) {
            // signup flow:
            String useremail = authUtil.determineUsernameFromOAuth2User(oAuth2User, registrationId, providerId);
            user = signUpInternal(new Request(useremail, null), providerType,name, providerId);
        } else if(user != null) {
            if(email != null && !email.isBlank() && !email.equals(user.getUsername())) {
                user.setEmail(email);
                userRepository.save(user);
            }
        } else {
            throw new BadCredentialsException("This email is already registered with provider "+emailUser.getProviderType());
        }

        Response loginResponseDto = new Response(authUtil.generateAccessToken(user), user.getEmail(), user.getAuthorities().toString(),user.getName());
        return ResponseEntity.ok(loginResponseDto);
    }
}
    
