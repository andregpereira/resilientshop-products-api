package com.github.andregpereira.resilientshop.productsapi.dtos.categoria;

import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaRegistroDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoriaRegistroDto(@NotBlank(message = "Insira o nome.") String nome,
		@NotNull(message = "Insira a subcategoria.") @Valid SubcategoriaRegistroDto subcategoria) {
}
