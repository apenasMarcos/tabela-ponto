package br.com.tabelaponto.util;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public LocalTime deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        String timeStr = parser.getValueAsString();
        return LocalTime.parse(timeStr, TIME_FORMATTER);
    }
}

