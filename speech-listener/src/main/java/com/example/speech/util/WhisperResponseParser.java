package com.example.speech.util;

import com.example.speech.web.dto.WhisperResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class WhisperResponseParser {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static WhisperResponse parse(byte[] bytes) {
        try {
            return OBJECT_MAPPER.readValue(bytes, WhisperResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
