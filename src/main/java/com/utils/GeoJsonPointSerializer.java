package com.utils;
import com.fasterxml.jackson.core.JsonGenerator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
 
import java.io.IOException;
 
public class GeoJsonPointSerializer extends JsonSerializer<GeoJsonPoint> {
    @Override
    public void serialize(GeoJsonPoint value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeStringField("type", value.getType());
        gen.writeArrayFieldStart("coordinates");
        gen.writeObject(value.getCoordinates());
        gen.writeEndArray();
        gen.writeEndObject();
    }
 
}
