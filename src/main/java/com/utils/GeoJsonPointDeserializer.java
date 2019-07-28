package com.utils;

import java.io.IOException;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class GeoJsonPointDeserializer extends JsonDeserializer<GeoJsonPoint> {

	@Override
	public GeoJsonPoint deserialize(JsonParser jsonParser, DeserializationContext ctxt)
			throws IOException {
	       ObjectCodec oc = jsonParser.getCodec();
	        JsonNode node = oc.readTree(jsonParser);
	        return new GeoJsonPoint(node.get("x").asDouble(), node.get("y").asDouble());
	  
	}

}
