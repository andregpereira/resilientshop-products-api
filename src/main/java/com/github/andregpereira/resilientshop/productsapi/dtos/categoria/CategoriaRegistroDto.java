package com.github.andregpereira.resilientshop.productsapi.dtos.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRegistroDto(@NotBlank(message = "O nome é obrigatório") @Size(min = 3, max = 45,
        message = "O nome não pode ter menos de 3 e mais de 45 caracteres") String nome) {

}
