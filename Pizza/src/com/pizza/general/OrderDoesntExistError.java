package com.pizza.general;

public class OrderDoesntExistError extends PizzaError {
	
	private static final long serialVersionUID = -3668011625570724170L;
	
	private final int orderId;
	
	public OrderDoesntExistError(int orderId) {
		this.orderId = orderId;
	}
	
	public String toString() {
		return "Order with id = " + orderId + "doesn't exist";
	}
}
