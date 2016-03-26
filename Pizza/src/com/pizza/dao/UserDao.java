package com.pizza.dao;

import java.util.List;

import com.pizza.model.HUser;

public interface UserDao {
	
    void saveUser(HUser user);
    
//    void updateUser(HUser user);
//    
    List<HUser> findAllUsers();
     
//    void deleteUserById(String userId);
//     
//    HUser findById(String userId);     
}
