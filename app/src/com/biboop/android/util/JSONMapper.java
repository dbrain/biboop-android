package com.biboop.android.util;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.PropertyNamingStrategy;
import org.codehaus.jackson.map.SerializationConfig;

public final class JSONMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(SerializationConfig.Feature.AUTO_DETECT_GETTERS, false);
        objectMapper.configure(SerializationConfig.Feature.USE_ANNOTATIONS, false);
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationConfig.Feature.AUTO_DETECT_SETTERS, false);
        objectMapper.configure(DeserializationConfig.Feature.USE_ANNOTATIONS, false);
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(new InconsistentNamingStrategy());
    }

    public static ObjectMapper getInstance() {
        return objectMapper;
    }

    public static class InconsistentNamingStrategy extends PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy {
        @Override
        public String translate(String property) {
            if (property != null && property.contains("_")) {
                return super.translate(property);
            }
            return property;

        }
    }
}

