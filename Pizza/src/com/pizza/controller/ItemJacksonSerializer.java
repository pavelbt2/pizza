package com.pizza.controller;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.pizza.model.Item;

// TODO move to a different package
public class ItemJacksonSerializer extends JsonSerializer<Item> {

	@Override
	public void serialize(Item item, JsonGenerator generator, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		
	    generator.writeStartObject();
//	    generator.writeFieldName("enum");
//	    generator.writeNumber(item.toString());
	    generator.writeFieldName("pretty");
	    generator.writeString(item.getPretty());
	    generator.writeEndObject();
	}

}
