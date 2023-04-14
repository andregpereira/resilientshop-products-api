package com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria;

import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaDto;

public record SubcategoriaDetalhesDto(Long id,
        String nome,
        String descricao,
        CategoriaDto categoria) {

}
