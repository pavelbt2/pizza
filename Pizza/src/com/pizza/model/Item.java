package com.pizza.model;

public enum Item {
	PIZZA_SLICE("Pizza Slice"),
	GREEK_SALAD("Greek Salad"),
	KALZONE("Kalzone");
	
	private final String prettyName;
	
	private Item (String prettyName) {
		this.prettyName = prettyName;
	}
	
	public String getPrettyName() {
		return prettyName;
	}
	
}
