package com.github.andregpereira.resilientshop.productsapi.util.json;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static com.github.andregpereira.resilientshop.productsapi.util.constant.CommonConstants.OBJECT_MAPPER;

@UtilityClass
public class JsonReader {

    private static final ResourceLoader RESOURCE_LOADER = new DefaultResourceLoader();

    private static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<>() {};

    public  <T> T read(final String filePath, Class<T> clazz) {
        try {
            final var map = OBJECT_MAPPER.readValue(RESOURCE_LOADER
                .getResource(filePath)
                .getInputStream(), MAP_TYPE_REFERENCE);
            map.computeIfPresent("data_criacao", JsonReader::parseDate);
            map.computeIfPresent("data_modificacao", JsonReader::parseDate);
            return OBJECT_MAPPER.convertValue(map, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object parseDate(String k, Object v) {
        return LocalDateTime.parse(String.valueOf(v), DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm"));
    }

}
