package com.github.andregpereira.resilientshop.productsapi.services.subcategoria;

import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubcategoriaConsultaService {

    Page<SubcategoriaDto> listar(Pageable pageable);

    SubcategoriaDetalhesDto consultarPorId(Long id);

}
