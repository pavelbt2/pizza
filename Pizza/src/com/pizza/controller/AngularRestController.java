package com.pizza.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.pizza.general.OrderDoesntExistError;
import com.pizza.general.UserAlreadyExistsError;
import com.pizza.model.HOrder;
import com.pizza.model.HUser;
import com.pizza.service.OrderService;
import com.pizza.service.UserService;
 

@CrossOrigin // TODO OK???  
@RestController
// maps incoming requests to methods and responses in json format
public class AngularRestController {
	
	static Logger log = Logger.getLogger(AngularRestController.class.getName());
  
    @Autowired
    UserService userService;
    
    @Autowired
    OrderService orderService;


    //------------------- Orders --------------------------------------------------------
    @RequestMapping(value = "/order/fetchall", method = RequestMethod.GET)
    public ResponseEntity<List<HOrder>> listAllOrders() {
    	log.info("listAllOrders()");
        List<HOrder> orders = orderService.findAllOrders();

        return new ResponseEntity<List<HOrder>>(orders, HttpStatus.OK);
    }   
    
    @RequestMapping(value = "/order/update/{id}", method = RequestMethod.POST)
    public ResponseEntity<HOrder> updateOrder(@RequestBody HOrder order, UriComponentsBuilder ucBuilder) {
    	log.info("Updating order:" + order.toString());
  
        try {
        	orderService.updateOrder(order);
        	
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path("/order/{id}").buildAndExpand(order.getId()).toUri()); // TODO ??? why error on console??
            return new ResponseEntity<HOrder>(headers, HttpStatus.OK);
        } catch (OrderDoesntExistError eExists) {
        	log.info("Order with id " + order.getId() + " not found");
        	return new ResponseEntity<HOrder>(HttpStatus.NOT_FOUND);      	
        } catch (Exception e) {
        	log.error("Unexpected error updating order:" + order.toString());
            return new ResponseEntity<HOrder>(HttpStatus.INTERNAL_SERVER_ERROR);        	
        }
                
    }

    @RequestMapping(value = "/order/create", method = RequestMethod.POST)
    public ResponseEntity<HOrder> createOrder(@RequestBody HOrder order, UriComponentsBuilder ucBuilder) {
    	log.info("Creating Order:"+order.toString());
  
        try {
        	orderService.createOrder(order);
        	
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path("/order/{id}").buildAndExpand(order.getId()).toUri()); // TODO ??? why error on console??
            return new ResponseEntity<HOrder>(order, headers, HttpStatus.OK);     	
        } catch (Exception e) {
        	log.info("Unexpected error creating order:" + order.toString());
            return new ResponseEntity<HOrder>(HttpStatus.INTERNAL_SERVER_ERROR);        	
        }
                
    }    
    
    //-------------------Retrieve All Users--------------------------------------------------------
      
    @RequestMapping(value = "/user/fetchall", method = RequestMethod.GET)
    public ResponseEntity<List<HUser>> listAllUsers() {
    	log.info("listAllUsers()");
        List<HUser> users = userService.findAllUsers();
        if(users.isEmpty()){
            return new ResponseEntity<List<HUser>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<HUser>>(users, HttpStatus.OK);
    }    
    
//  
//     
//    //-------------------Retrieve Single User--------------------------------------------------------
//      
//    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
//        System.out.println("Fetching User with id " + id);
//        User user = userService.findById(id);
//        if (user == null) {
//            System.out.println("User with id " + id + " not found");
//            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<User>(user, HttpStatus.OK);
//    }
//  
//      
//      
    //-------------------Create a User--------------------------------------------------------
      
    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody HUser user,    UriComponentsBuilder ucBuilder) {
    	log.info("Creating User:" + user.getFirstName() + " "+ user.getLastName());
  
        try {
        	userService.addUser(user);
        	
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri()); // TODO ??? why error on console??
            return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
        } catch (UserAlreadyExistsError eExists) {
        	log.info("User already exists:" + user.getFirstName() + " "+ user.getLastName());
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);        	
        } catch (Exception e) {
        	log.info("Unexpected error creating user:" + user.getFirstName() + " "+ user.getLastName());
            return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);        	
        }
                
    }
//  
//     
//      
//    //------------------- Update a User --------------------------------------------------------
//      
//    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
//    public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user) {
//        System.out.println("Updating User " + id);
//          
//        User currentUser = userService.findById(id);
//          
//        if (currentUser==null) {
//            System.out.println("User with id " + id + " not found");
//            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
//        }
//  
//        currentUser.setUsername(user.getUsername());
//        currentUser.setAddress(user.getAddress());
//        currentUser.setEmail(user.getEmail());
//          
//        userService.updateUser(currentUser);
//        return new ResponseEntity<User>(currentUser, HttpStatus.OK);
//    }
//  
//     
//     
//    //------------------- Delete a User --------------------------------------------------------
//      
//    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
//    public ResponseEntity<User> deleteUser(@PathVariable("id") long id) {
//        System.out.println("Fetching & Deleting User with id " + id);
//  
//        User user = userService.findById(id);
//        if (user == null) {
//            System.out.println("Unable to delete. User with id " + id + " not found");
//            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
//        }
//  
//        userService.deleteUserById(id);
//        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
//    }
//  
//      
//     
//    //------------------- Delete All Users --------------------------------------------------------
//      
//    @RequestMapping(value = "/user/", method = RequestMethod.DELETE)
//    public ResponseEntity<User> deleteAllUsers() {
//        System.out.println("Deleting All Users");
//  
//        userService.deleteAllUsers();
//        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
//    }
  
}

//ppp