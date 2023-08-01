package com.group3.mBaaS.analytics;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

// Converts a String from the database to JSON
@ReadingConverter
public class ToJSONConverter implements Converter<String, Map<String, Object>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, Object> convert(String json) {
        Map<String, Object> jsonObject = Collections.emptyMap();
        try {
            if (!json.isEmpty()) {
                jsonObject = objectMapper.readValue(json, Map.class);
            }
        } catch (final IOException e) {
            System.out.println(e.toString());
        }

        return jsonObject;
    }
}
