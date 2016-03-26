package com.pizza.service;

import java.util.List;

import com.pizza.model.HUser;

public interface UserService {

    void saveUser(HUser user);

	List<HUser> findAllUsers();
	
}
