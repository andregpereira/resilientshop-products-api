package com.github.andregpereira.resilientshop.productsapi.app.services.subcategoria;

import com.github.andregpereira.resilientshop.productsapi.app.dtos.subcategoria.SubcategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.subcategoria.SubcategoriaRegistroDto;

public interface SubcategoriaManutencaoService {

    SubcategoriaDetalhesDto criar(SubcategoriaRegistroDto dto);

    SubcategoriaDetalhesDto atualizar(Long id, SubcategoriaRegistroDto dto);

    String remover(Long id);

}
