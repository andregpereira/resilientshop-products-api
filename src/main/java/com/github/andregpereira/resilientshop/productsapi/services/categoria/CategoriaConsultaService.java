package com.github.andregpereira.resilientshop.productsapi.services.categoria;

import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoriaConsultaService {

    Page<CategoriaDto> listar(Pageable pageable);

    CategoriaDto consultarPorId(Long id);

}
