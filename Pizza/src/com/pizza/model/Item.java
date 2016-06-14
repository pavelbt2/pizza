package com.pizza.model;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pizza.jackson.ItemJacksonDeSerializer;
import com.pizza.jackson.ItemJacksonSerializer;


@JsonSerialize(using = ItemJacksonSerializer.class)
@JsonDeserialize(using = ItemJacksonDeSerializer.class)
// TODO @JsonValue??
public enum Item {
	PIZZA_SLICE("Pizza Slice", 12.5),
	GREEK_SALAD("Greek Salad", 27.0),
	HOME_SALAD("Home Salad", 25.0),
	TUNA_SALAD("Tuna Salad", 25.0),
	KALZONE("Kalzone", 25.0),
	PASTA("Pasta", 28.0),
	HOME_BREAD("Home Bread", 5.0),
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

