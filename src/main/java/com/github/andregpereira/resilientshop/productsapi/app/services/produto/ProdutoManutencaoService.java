package com.github.andregpereira.resilientshop.productsapi.services.produto;

import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoAtualizacaoDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoRegistroDto;

public interface ProdutoManutencaoService {

    ProdutoDetalhesDto registrar(ProdutoRegistroDto dto);

    ProdutoDetalhesDto atualizar(Long id, ProdutoAtualizacaoDto dto);

    String remover(Long id);

}
