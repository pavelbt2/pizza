package com.pizza.service;

import java.util.List;

import com.pizza.general.UserAlreadyExistsError;
import com.pizza.model.HUser;

public interface UserService {

	List<HUser> findAllUsers();
	
    void addUser(HUser user) throws UserAlreadyExistsError;
	
}
