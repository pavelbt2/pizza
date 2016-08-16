package com.pizza.error;

import java.util.Date;

public class OrderAlreadyExistError extends PizzaError {
		
	private static final long serialVersionUID = 8623166917834393867L;
	
	private final Date date;
	
	public OrderAlreadyExistError(Date date) {
		this.date = date;
	}
	
	public String toString() {
		return "Order for date = " + date + "already exists";
	}
}
