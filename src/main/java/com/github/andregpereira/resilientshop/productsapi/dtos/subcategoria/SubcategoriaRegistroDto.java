package com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria;

import jakarta.validation.constraints.NotBlank;

public record SubcategoriaRegistroDto(@NotBlank(message = "Insira o nome da subcategoria.") String nome,
		@NotBlank(message = "Insira a descrição.") String descricao) {
}
