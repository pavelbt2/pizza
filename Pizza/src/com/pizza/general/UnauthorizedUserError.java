package com.pizza.general;

public class UnauthorizedUserError extends PizzaError {
		
	private static final long serialVersionUID = -6928289557048963125L;

	public UnauthorizedUserError() {
	}
	
	public String toString() {
		return "Logged in user is unauthorized for this operatin on requested order";
	}
}
