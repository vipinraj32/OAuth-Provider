package com.oauthprovider.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oauthprovider.DTO.Request;
import com.oauthprovider.DTO.Response;
import com.oauthprovider.entity.User;
import com.oauthprovider.service.AuthService;

@RestController
@RequestMapping("/user")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody Request loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody User user){
		User u=authService.signup(user);
		Map<String,Object>response= new HashMap<>();
		response.put("message","Successfully Register");
		response.put("status",HttpStatus.CREATED.value());
	    return ResponseEntity.ok(response);	
	}
//	@PreAuthorize("hasRole('USER')")
	@GetMapping("/showAll")
	public ResponseEntity<List<User>>show(){
		
		List<User> users=authService.showAll();
    	return new ResponseEntity<>(users,HttpStatus.OK);
	}

	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/welcome")
	public ResponseEntity<?> welcome(){
		Map<String,String> response=new HashMap<>();
		response.put("name","Hello ");
		response.put("status","ok");
		return ResponseEntity.ok(response);
	}
}
