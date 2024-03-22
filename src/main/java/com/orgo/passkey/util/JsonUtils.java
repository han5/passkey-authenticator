package com.orgo.passkey.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    private static final ObjectMapper generalJsonMapper = new ObjectMapper();

    public static <T> Function<T,String> newJsonWrites() {
        return newJsonWrites(generalJsonMapper);
    }

    public static <T> Function<T,String> newJsonWrites( final ObjectMapper jmapper ) {
        return (input) -> {
            return withObjectMapper( jmapper, input );
        };
    }

    public static Function<Map<String,?>,String> newJsonWritesMap() {
        return (input) -> {
            return withObjectMapper(generalJsonMapper, input);
        };
    }

    public static Function<Map<String,?>,String> newJsonWritesMap( final ObjectMapper jmapper ) {
        return (input) -> {
            return withObjectMapper( jmapper, input );
        };
    }

    private static <T> String withObjectMapper( final ObjectMapper mapper, final T input ) {
        if(input == null) {
            return null;
        }
        try {
            return mapper.writeValueAsString(input);
        } catch(Exception e) {
            throw new IllegalArgumentException("json_mapper_fails=" + e.getMessage());
        }
    }

    public static <T> Function<String,T> newJsonReads( final Class<T> type ) {
        return newJsonReads(generalJsonMapper, type);
    }

    public static <T> Function<String,T> newJsonReads( final ObjectMapper mapper, final Class<T> type ) {
        return (input) -> {
            if(input == null) {
                return null;
            }
            try {
                return mapper.readValue(input, type);
            } catch(Exception e) {
                throw new IllegalArgumentException("json_mapper_fails=" + e.getMessage());
            }
        };
    }

    public static <T> Function<Map<String,?>,T> newJsonReadsMap( final ObjectMapper mapper, final Class<T> type ) {
        return (input) -> {
            if(input == null) {
                return null;
            }
            try {
                return mapper.convertValue(input, type);
            } catch(Exception e) {
                throw new IllegalArgumentException("json_mapper_fails=" + e.getMessage());
            }
        };
    }

    private static final TypeReference<HashMap<String,Object>> mapTypeRef = new TypeReference<HashMap<String,Object>>() {};

    public static Function<String,Map<String,?>> newJsonReadStringToMap() {
        return newJsonReadStringToMap(generalJsonMapper);
    }

    public static <T> Function<String,Map<String,?>> newJsonReadStringToMap( final ObjectMapper mapper ) {
        return (input) -> {
            if(input == null) {
                return null;
            }
            try {
                return mapper.readValue(input, mapTypeRef);
            } catch(Exception e) {
                throw new IllegalArgumentException("json_mapper_fails=" + e.getMessage());
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getJsonClassType( T input ) {
        return (Class<T>) input.getClass();
    }
}