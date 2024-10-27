package com.github.andregpereira.resilientshop.productsapi.config;

import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.TreeMap;

import static java.util.Comparator.comparing;

@Configuration
public class SwaggerConfig {

    private final Contact contact = new Contact()
        .name("André Garcia")
        .email("61523970+andregpereira@users.noreply.github.com")
        .url("https://github.com/andregpereira");

    private final Info info = new Info()
        .title("API de Produtos")
        .description("API do microsserviço de Produtos do e-Commerce Resilient Shop.")
        .contact(contact)
        .version("1.0.0");

    private void sortTags(final List<Tag> tags) {
        tags.sort(comparing(Tag::getName));
    }

    private void sortPaths(final Paths paths) {
        final var treeMap = new TreeMap<>(paths);
        paths.clear();
        paths.putAll(treeMap);
    }

    @Bean
    OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            sortTags(openApi.getTags());
            sortPaths(openApi.getPaths());
            openApi.info(info);
        };
    }

}
