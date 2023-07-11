package com.github.andregpereira.resilientshop.productsapi.app.dto.produto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.github.andregpereira.resilientshop.productsapi.app.dto.subcategoria.SubcategoriaDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoDetalhesDto(Long id,
        Long sku,
        String nome,
        String descricao,
        @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/uuuu HH:mm") LocalDateTime dataCriacao,
        BigDecimal valorUnitario,
        int estoque,
        boolean ativo,
        SubcategoriaDto subcategoria) {

}