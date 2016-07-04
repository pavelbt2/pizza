package com.pizza.configuration;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PizzaPasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		if (rawPassword == null) {
			return null;
		}
		String encoded = rawPassword.toString() + rawPassword.toString();
		return encoded;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if (rawPassword == null || encodedPassword == null) {
			return false;
		}
		String encodedRaw = encode(rawPassword);
		boolean equal = encodedRaw.equals(encodedPassword);
		return equal;
	}

}
