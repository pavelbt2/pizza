package com.pizza.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pizza.jackson.OrderStatusJacksonDeSerializer;
import com.pizza.jackson.OrderStatusJacksonSerializer;

@JsonSerialize(using = OrderStatusJacksonSerializer.class)
@JsonDeserialize(using = OrderStatusJacksonDeSerializer.class)
public enum OrderStatus {

	OPEN("Open"),
	ORDERED("Ordered"),
	RECEIVED("Received")
	;
	
	private final String pretty;
	
	private OrderStatus(String pretty) {
		this.pretty = pretty;
	}
	
	public String getPretty() {
		return pretty;
	}

}
