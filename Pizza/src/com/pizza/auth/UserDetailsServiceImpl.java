package com.pizza.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	PasswordEncoder passwordEncoder;
	
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	return new JwtUser(username, passwordEncoder.encode("1234"));
    }
}
