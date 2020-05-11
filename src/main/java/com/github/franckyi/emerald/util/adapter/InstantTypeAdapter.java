package com.github.franckyi.emerald.util.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class InstantTypeAdapter implements JsonDeserializer<Instant> {

    @Override
    public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String instant = json.getAsString();
        try {
            return DateTimeFormatter.ISO_INSTANT.parse(instant, Instant::from);
        } catch (DateTimeParseException e) {
            throw new JsonParseException(String.format("Unable to parse date '%s'", instant), e);
        }
    }
}
