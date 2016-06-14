package com.pizza.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.pizza.model.OrderStatus;

public class OrderStatusJacksonDeSerializer extends JsonDeserializer<OrderStatus> {

	@Override
	public OrderStatus deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
	     ObjectCodec oc = jp.getCodec();
	        JsonNode node = oc.readTree(jp);
	        String id = node.get("id").textValue();
	        OrderStatus status =  OrderStatus.valueOf(id);
	        return status;
	}



}
