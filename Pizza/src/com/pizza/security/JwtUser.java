package com.pizza.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JwtUser implements UserDetails {

	private static final long serialVersionUID = 8154473889668054911L;
	private final String username;
	private final String password;

    public JwtUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<GrantedAuthority>();
	}

    @JsonIgnore
	@Override
	public String getPassword() {
    	return password;
	}

    @JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

    @JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

    @JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

    @JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}
}
