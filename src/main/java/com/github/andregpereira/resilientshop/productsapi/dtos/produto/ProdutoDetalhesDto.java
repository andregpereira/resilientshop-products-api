package com.github.andregpereira.resilientshop.productsapi.dtos.produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Builder;

@Builder
public record ProdutoDetalhesDto(Long id, Long sku, String nome, String descricao,
		@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy - HH:mm") LocalDateTime dataCriacao,
		BigDecimal valorUnitario, int estoque) {
}
