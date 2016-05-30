package com.pizza.model;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pizza.controller.ItemJacksonSerializer;

@JsonSerialize(using = ItemJacksonSerializer.class)
public enum Item {
	PIZZA_SLICE("Pizza Slice", 12.5),
	GREEK_SALAD("Greek Salad", 27.0),
	HOME_SALAD("סלט הבית", 25.0),
	KALZONE("Kalzone", 25.0),
	;
	
	private final String pretty;
	private final double price;
	
	private Item (String pretty, double price) {
		this.pretty = pretty;
		this.price = price;
	}
	
	public String getPretty() {
		return pretty;
	}
	
	public double getPrice() {
		return price;
	}

	public static List<Item> getAllItemsAsList() {
		return Arrays.asList(Item.values());
	}	
	
}
