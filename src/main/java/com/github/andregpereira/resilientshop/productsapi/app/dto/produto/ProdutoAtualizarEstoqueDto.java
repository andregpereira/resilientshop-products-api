package com.github.andregpereira.resilientshop.productsapi.app.dto.produto;

public record ProdutoAtualizarEstoqueDto(
    Long id,
    int quantidade
) {}
