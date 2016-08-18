package com.pizza.error;

import com.pizza.model.Item;

public class EmailError extends PizzaError {
				
	private static final long serialVersionUID = 3843997463119973256L;

	public EmailError() {
	}
	
	public String toString() {
		return "Error sending email";
	}
}
