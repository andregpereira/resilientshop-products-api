package com.github.andregpereira.resilientshop.productsapi.app.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.json.JsonSmartJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.openfeign.support.PageJacksonModule;
import org.springframework.cloud.openfeign.support.SortJacksonModule;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.databind.DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS;
import static com.fasterxml.jackson.databind.DeserializationFeature.USE_LONG_FOR_INTS;
import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE;

@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
@TestConfiguration
public class ObjectMapperTestConfig {

    private static final List<Module> MODULES =
        List.of(new JavaTimeModule(), new PageJacksonModule(), new SortJacksonModule());

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            builder
                .propertyNamingStrategy(SNAKE_CASE)
                .modules(MODULES)
                .featuresToEnable(USE_LONG_FOR_INTS, USE_BIG_DECIMAL_FOR_FLOATS);
        };
    }

    @Bean
    public Configuration configuration(final ObjectMapper objectMapper) {
        Configuration.setDefaults(new Configuration.Defaults() {
            @Override
            public JsonProvider jsonProvider() {
                return new JsonSmartJsonProvider();
            }

            @Override
            public MappingProvider mappingProvider() {
                return new JacksonMappingProvider(objectMapper);
            }

            @Override
            public Set<Option> options() {
                return EnumSet.noneOf(Option.class);
            }
        });
        return Configuration.defaultConfiguration();
    }

}
