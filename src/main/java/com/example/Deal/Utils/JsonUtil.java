package com.example.Deal.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Provides means for object deserialize from JSON format.
 */
public final class JsonUtil {

    private JsonUtil() {}

    /**
     * Deserializes byte array to some Class instance.
     *
     * @param bytes value must be deserialized
     * @param type Class type to which must be converted
     * @return deserialized instance
     */
    public static Object deserialize(byte[] bytes, Class<?> type) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(bytes, type);
    }

}
