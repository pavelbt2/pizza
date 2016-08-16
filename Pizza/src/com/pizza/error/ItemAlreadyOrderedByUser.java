package com.pizza.error;

import com.pizza.model.Item;

public class ItemAlreadyOrderedByUser extends PizzaError {
			
	private static final long serialVersionUID = 661530286911641431L;
	
	private final Item item;
    private final String user;
	
	public ItemAlreadyOrderedByUser(Item item, String user) {
		this.item = item;
		this.user = user;
	}
	
	public String toString() {
		return "User " + user + "already ordered " + item;
	}
}
