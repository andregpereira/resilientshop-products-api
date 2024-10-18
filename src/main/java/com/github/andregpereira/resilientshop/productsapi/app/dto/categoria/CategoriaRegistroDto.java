package com.github.andregpereira.resilientshop.productsapi.app.dto.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRegistroDto(
    @NotBlank(message = "O nome é obrigatório")
    @Size(
        message = "O nome não pode ter menos de 3 e mais de 45 caracteres",
        min = 3,
        max = 45
    )
    String nome
) {}
