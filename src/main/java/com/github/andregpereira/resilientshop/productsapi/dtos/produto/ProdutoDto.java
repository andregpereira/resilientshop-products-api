package com.github.andregpereira.resilientshop.productsapi.dtos.produto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoDto(Long id, String nome, String descricao,
        @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy - HH:mm:ss") LocalDateTime dataCriacao,
        BigDecimal valorUnitario, int estoque) {

}
