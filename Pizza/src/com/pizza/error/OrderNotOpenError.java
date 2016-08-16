package com.pizza.error;

public class OrderNotOpenError extends PizzaError {
	
	private static final long serialVersionUID = -8456631645407830214L;
	
	private final long orderId;
	
	public OrderNotOpenError(long orderId) {
		this.orderId = orderId;
	}
	
	public String toString() {
		return "Order with id = " + orderId + "isn't open";
	}
}
