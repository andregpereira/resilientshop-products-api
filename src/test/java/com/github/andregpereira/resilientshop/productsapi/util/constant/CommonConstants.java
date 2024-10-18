package com.github.andregpereira.resilientshop.productsapi.util.constant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;

import static com.fasterxml.jackson.databind.DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS;
import static com.fasterxml.jackson.databind.DeserializationFeature.USE_LONG_FOR_INTS;
import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public interface CommonConstants {

    String MOCKS_PATH = "classpath:mock";

    ObjectMapper OBJECT_MAPPER = buildObjectMapper();

    JsonPathResultMatchers ROOT_JSON_PATH = jsonPath("$");

    Pageable PAGEABLE_ID = PageRequest.of(0, 10, Direction.ASC, "id");

    Pageable PAGEABLE_NOME = PageRequest.of(0, 10, Direction.ASC, "nome");

    private static ObjectMapper buildObjectMapper() {
        return Jackson2ObjectMapperBuilder
            .json()
            .propertyNamingStrategy(SNAKE_CASE)
            .modules(new JavaTimeModule())
            .featuresToEnable(USE_LONG_FOR_INTS, USE_BIG_DECIMAL_FOR_FLOATS)
            .build();
    }

}
