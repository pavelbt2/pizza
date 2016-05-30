package com.pizza.controller;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.pizza.model.Item;

// TODO move to a different package
public class ItemJacksonDeSerializer extends JsonDeserializer<Item> {

	@Override
	public Item deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
	     ObjectCodec oc = jp.getCodec();
	        JsonNode node = oc.readTree(jp);
	        String id = node.get("id").textValue();
	        Item item =  Item.valueOf(id);
	        return item;
	}



}
