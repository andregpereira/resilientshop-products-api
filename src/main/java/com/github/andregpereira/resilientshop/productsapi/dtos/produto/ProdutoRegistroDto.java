package com.github.andregpereira.resilientshop.productsapi.dtos.produto;

import java.math.BigDecimal;

import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaRegistroDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProdutoRegistroDto(@NotNull(message = "Insira o SKU.") Long sku,
		@NotBlank(message = "Insira o nome.") String nome, @NotBlank(message = "Insira a descrição.") String descricao,
		@NotNull(message = "Insira o valor unitário.") BigDecimal valorUnitario,
		@NotNull(message = "Insira o estoque.") @Min(message = "O estoque mínimo deve ser 0.", value = 0) int estoque,
		@NotNull(message = "Insira a categoria.") @Valid CategoriaRegistroDto categoria) {
}
