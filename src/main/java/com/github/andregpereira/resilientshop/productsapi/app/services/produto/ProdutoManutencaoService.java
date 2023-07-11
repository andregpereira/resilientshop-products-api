package com.github.andregpereira.resilientshop.productsapi.app.services.produto;

import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoAtualizacaoDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoAtualizarEstoqueDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoRegistroDto;

import java.util.Set;

public interface ProdutoManutencaoService {

    ProdutoDetalhesDto criar(ProdutoRegistroDto dto);

    ProdutoDetalhesDto atualizar(Long id, ProdutoAtualizacaoDto dto);

    String desativar(Long id);

    String reativar(Long id);

    void subtrairEstoque(Set<ProdutoAtualizarEstoqueDto> dtos);

    void retornarEstoque(Set<ProdutoAtualizarEstoqueDto> dtos);

}
