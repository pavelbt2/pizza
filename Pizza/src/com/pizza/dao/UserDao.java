package com.pizza.dao;

import java.util.List;

import com.pizza.general.UserAlreadyExistsError;
import com.pizza.model.HUser;

public interface UserDao {
	
    List<HUser> findAllUsers();
    
    void saveUser(HUser user) throws UserAlreadyExistsError;
    
//    void updateUser(HUser user);
//    
     
//    void deleteUserById(String userId);
//     
//    HUser findById(String userId);     
}
