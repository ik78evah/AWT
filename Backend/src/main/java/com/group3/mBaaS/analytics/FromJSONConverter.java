package com.group3.mBaaS.analytics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.util.Map;

// Converts JSON to String for saving it in the SQL database
@WritingConverter
public class FromJSONConverter implements Converter<Map<String, Object>, String> {

    final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convert(Map<String, Object> source) {
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(source);
        } catch (final JsonProcessingException e) {
            System.out.println(e.toString());
        }

        return jsonString;
    }
}
