package com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria;

import org.springframework.data.domain.Page;

import com.github.andregpereira.resilientshop.productsapi.entities.Subcategoria;

import lombok.Builder;

@Builder
public record SubcategoriaDto(Long id, String nome, String descricao) {

	public SubcategoriaDto(Subcategoria subcategoria) {
		this(subcategoria.getId(), subcategoria.getNome(), subcategoria.getDescricao());
	}

	public static Page<SubcategoriaDto> criarPage(Page<Subcategoria> subcategorias) {
		return subcategorias.map(SubcategoriaDto::new);
	}

}
