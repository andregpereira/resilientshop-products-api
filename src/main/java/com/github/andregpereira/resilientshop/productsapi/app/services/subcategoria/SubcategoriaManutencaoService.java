package com.github.andregpereira.resilientshop.productsapi.services.subcategoria;

import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaRegistroDto;

public interface SubcategoriaManutencaoService {

    SubcategoriaDetalhesDto registrar(SubcategoriaRegistroDto dto);

    SubcategoriaDetalhesDto atualizar(Long id, SubcategoriaRegistroDto dto);

    String remover(Long id);

}
