package com.pizza.model;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pizza.controller.ItemJacksonSerializer;

@JsonSerialize(using = ItemJacksonSerializer.class)
public enum Item {
	PIZZA_SLICE("Pizza Slice"),
	GREEK_SALAD("Greek Salad"),
	KALZONE("Kalzone");
	
	private final String pretty;
	
	private Item (String pretty) {
		this.pretty = pretty;
	}
	
	public String getPretty() {
		return pretty;
	}
	
	public static List<Item> getAllItemsAsList() {
		return Arrays.asList(Item.values());
	}	
	
}
