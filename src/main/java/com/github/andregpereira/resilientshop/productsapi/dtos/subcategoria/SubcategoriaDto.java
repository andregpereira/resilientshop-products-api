package com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria;

import com.github.andregpereira.resilientshop.productsapi.entities.Subcategoria;

import lombok.Builder;

@Builder
public record SubcategoriaDto(Long id, String nome, String descricao) {

	public SubcategoriaDto(Subcategoria subcategoria) {
		this(subcategoria.getId(), subcategoria.getNome(), subcategoria.getDescricao());
	}

}
