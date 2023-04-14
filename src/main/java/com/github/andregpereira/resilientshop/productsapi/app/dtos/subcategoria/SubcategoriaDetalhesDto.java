package com.github.andregpereira.resilientshop.productsapi.app.dtos.subcategoria;

import com.github.andregpereira.resilientshop.productsapi.app.dtos.categoria.CategoriaDto;

public record SubcategoriaDetalhesDto(Long id,
        String nome,
        String descricao,
        CategoriaDto categoria) {

}
