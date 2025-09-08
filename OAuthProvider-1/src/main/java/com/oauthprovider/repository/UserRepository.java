package com.oauthprovider.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oauthprovider.entity.AuthProviderType;
import com.oauthprovider.entity.User;
import java.util.List;


public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByEmail(String email);

	Optional<User> findByProviderIdAndProviderType(String providerId,AuthProviderType authProviderType );
}
