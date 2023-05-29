package com.github.andregpereira.resilientshop.productsapi.app.dto.subcategoria;

import com.github.andregpereira.resilientshop.productsapi.app.dto.categoria.CategoriaDto;

public record SubcategoriaDetalhesDto(Long id,
        String nome,
        String descricao,
        CategoriaDto categoria) {

}
