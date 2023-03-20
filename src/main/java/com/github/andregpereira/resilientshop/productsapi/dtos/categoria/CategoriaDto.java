package com.github.andregpereira.resilientshop.productsapi.dtos.categoria;

import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Categoria;

import lombok.Builder;

@Builder
public record CategoriaDto(Long id, String nome, SubcategoriaDto subcategoria) {

	public CategoriaDto(Categoria categoria) {
		this(categoria.getId(), categoria.getNome(), new SubcategoriaDto(categoria.getSubcategoria()));
	}

}
