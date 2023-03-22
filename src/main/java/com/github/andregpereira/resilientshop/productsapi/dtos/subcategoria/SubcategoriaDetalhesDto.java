package com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria;

import org.springframework.data.domain.Page;

import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Subcategoria;

import lombok.Builder;

@Builder
public record SubcategoriaDetalhesDto(Long id, String nome, String descricao, CategoriaDto categoria) {

	public SubcategoriaDetalhesDto(Subcategoria subcategoria) {
		this(subcategoria.getId(), subcategoria.getNome(), subcategoria.getDescricao(),
				new CategoriaDto(subcategoria.getCategoria()));
	}

	public static Page<SubcategoriaDetalhesDto> criarPage(Page<Subcategoria> subcategorias) {
		return subcategorias.map(SubcategoriaDetalhesDto::new);
	}

}
