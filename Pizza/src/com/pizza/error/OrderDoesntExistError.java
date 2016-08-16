package com.pizza.error;

import java.util.Date;

public class OrderDoesntExistError extends PizzaError {
		
	private static final long serialVersionUID = 5217530599763754606L;
	
	private final long orderId;
	
	public OrderDoesntExistError(long orderId) {
		this.orderId = orderId;
	}
	
	public String toString() {
		return "Order with id = " + orderId + "doesn't exist";
	}
	
}
