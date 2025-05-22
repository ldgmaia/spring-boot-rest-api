package com.example.api.infra.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonPrinter {

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.INDENT_OUTPUT);

    public static void printJson(Object object) {
        try {
            String json = mapper.writeValueAsString(object);
            System.out.println(json);
        } catch (JsonProcessingException e) {
            System.err.println("Error converting object to JSON: " + e.getMessage());
        }
    }

    public static String toJsonString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return "Error converting object to JSON: " + e.getMessage();
        }
    }
}
