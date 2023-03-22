package com.github.andregpereira.resilientshop.productsapi.dtos.categoria;

import org.springframework.data.domain.Page;

import com.github.andregpereira.resilientshop.productsapi.entities.Categoria;

import lombok.Builder;

@Builder
public record CategoriaDto(Long id, String nome) {

	public CategoriaDto(Categoria categoria) {
		this(categoria.getId(), categoria.getNome());
	}

	public static Page<CategoriaDto> criarPage(Page<Categoria> categorias) {
		return categorias.map(CategoriaDto::new);
	}

}
