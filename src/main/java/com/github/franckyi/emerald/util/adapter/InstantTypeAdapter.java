package com.github.franckyi.emerald.util.adapter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class InstantTypeAdapter implements JsonSerializer<Instant>, JsonDeserializer<Instant> {
    private final List<DateTimeFormatter> formats;

    public InstantTypeAdapter() {
        formats = Arrays.asList(DateTimeFormatter.ISO_INSTANT, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    @Override
    public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    @Override
    public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String instant = json.getAsString();
        DateTimeParseException e = null;
        for (DateTimeFormatter formatter : formats) {
            try {
                return formatter.parse(instant, Instant::from);
            } catch (DateTimeParseException ex) {
                e = ex;
            }
        }
        throw new JsonParseException(String.format("Unable to parse date '%s'", instant), e);
    }
}
