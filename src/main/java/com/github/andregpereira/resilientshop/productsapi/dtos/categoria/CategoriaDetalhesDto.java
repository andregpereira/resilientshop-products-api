package com.github.andregpereira.resilientshop.productsapi.dtos.categoria;

import java.util.List;

import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDto;

import lombok.Builder;

@Builder
public record CategoriaDetalhesDto(Long id, String nome, List<SubcategoriaDto> subcategorias) {
}
