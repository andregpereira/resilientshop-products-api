package com.github.andregpereira.resilientshop.productsapi.app.services.categoria;

import com.github.andregpereira.resilientshop.productsapi.app.dto.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.categoria.CategoriaRegistroDto;

public interface CategoriaManutencaoService {

    CategoriaDto criar(CategoriaRegistroDto dto);

    CategoriaDto atualizar(Long id, CategoriaRegistroDto dto);

    String remover(Long id);

}
