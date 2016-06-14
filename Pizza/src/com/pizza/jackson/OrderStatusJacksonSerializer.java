package com.pizza.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.pizza.model.OrderStatus;

public class OrderStatusJacksonSerializer extends JsonSerializer<OrderStatus> {

	@Override
	public void serialize(OrderStatus status, JsonGenerator generator, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		
	    generator.writeStartObject();
	    generator.writeFieldName("id");
	    generator.writeString(status.toString());	    
	    generator.writeFieldName("pretty");
	    generator.writeString(status.getPretty());    
	    generator.writeEndObject();
	}

}
