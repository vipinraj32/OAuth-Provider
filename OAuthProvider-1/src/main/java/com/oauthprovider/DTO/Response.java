package com.oauthprovider.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
	
	private String token;
	private String email;
	private String role;
	private String username;

}
