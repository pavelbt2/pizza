package com.pizza.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.pizza.dao.UserDao;
import com.pizza.general.PizzaError;
import com.pizza.general.UserAlreadyExistsError;
import com.pizza.model.HUser;

@Service("userService")
@Transactional(propagation=Propagation.REQUIRED, rollbackFor={PizzaError.class})
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Override
	public void addUser(HUser user) throws UserAlreadyExistsError {
		userDao.saveUser(user);
	}

	@Override
	public List<HUser> findAllUsers() {
		return userDao.findAllUsers();
	}

}
