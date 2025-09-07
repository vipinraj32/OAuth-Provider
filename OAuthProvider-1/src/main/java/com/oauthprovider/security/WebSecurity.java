package com.oauthprovider.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Configuration
@AllArgsConstructor
@Slf4j
@EnableMethodSecurity
public class WebSecurity {

	
	private final JwtAuthFilter jwtAuthFilter;
	  private final HandlerExceptionResolver handlerExceptionResolver;
	  
	 
	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
	        httpSecurity
	                .csrf(csrfConfig -> csrfConfig.disable())
	                .sessionManagement(sessionConfig ->
	                        sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	                .authorizeHttpRequests(auth -> auth
	                        .requestMatchers("/user/login","/user/signup").permitAll()
	                        .anyRequest().authenticated()
	                )
	                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

//	                .formLogin();
	        return httpSecurity.build();
	    }
}
