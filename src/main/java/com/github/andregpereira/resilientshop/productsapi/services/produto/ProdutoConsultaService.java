package com.github.andregpereira.resilientshop.productsapi.services.produto;

import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProdutoConsultaService {

    Page<ProdutoDto> listar(Pageable pageable);

    ProdutoDetalhesDto consultarPorId(Long id);

    Page<ProdutoDto> consultarPorNome(String nome, Pageable pageable);

    Page<ProdutoDto> consultarPorSubcategoria(Long id, Pageable pageable);

    Page<ProdutoDto> consultarPorCategoria(Long id, Pageable pageable);

}
