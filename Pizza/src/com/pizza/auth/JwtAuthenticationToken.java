package com.pizza.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {
	private final String authToken;

	public JwtAuthenticationToken(String authToken) {
		super(authToken, null);
		// Object principal, Object credentials
		this.authToken = authToken;
	}

	public String getToken() {
		return authToken;
	}
}
