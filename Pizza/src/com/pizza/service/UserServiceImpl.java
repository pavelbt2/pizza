package com.pizza.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.pizza.dao.UserDao;
import com.pizza.model.HUser;

@Service("userService")
@Transactional(propagation=Propagation.REQUIRED)
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Override
	public void saveUser(HUser user) {
		userDao.saveUser(user);		
	}

	@Override
	public List<HUser> findAllUsers() {
		return userDao.findAllUsers();
	}

}
