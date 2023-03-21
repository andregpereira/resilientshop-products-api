package com.github.andregpereira.resilientshop.productsapi.dtos.categoria;

import com.github.andregpereira.resilientshop.productsapi.entities.Categoria;

import lombok.Builder;

@Builder
public record CategoriaDetalhesDto(Long id, String nome) {

	public CategoriaDetalhesDto(Categoria categoria) {
		this(categoria.getId(), categoria.getNome());
	}

}
