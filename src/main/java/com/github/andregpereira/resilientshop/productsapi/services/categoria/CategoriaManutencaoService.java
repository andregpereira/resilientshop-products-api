package com.github.andregpereira.resilientshop.productsapi.services.categoria;

import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaRegistroDto;

public interface CategoriaManutencaoService {

    CategoriaDto registrar(CategoriaRegistroDto dto);

    CategoriaDto atualizar(Long id, CategoriaRegistroDto dto);

    String remover(Long id);

}
