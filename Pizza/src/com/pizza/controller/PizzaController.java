package com.pizza.controller;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * This is the controller class
 *
 */

@Controller // this specifies that this is a controller class
@Scope(value="singleton")
// "singleton" - is the default
// can use "prototype" for an instance to be created per access. Or other values..
public class PizzaController {
	
	static Logger log = Logger.getLogger(PizzaController.class.getName());
	
	@RequestMapping("/")
	public ModelAndView index() {
		return new ModelAndView("WEB-INF/jsp/UserManagement.jsp", "res", null);
	}


}

