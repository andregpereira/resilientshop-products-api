package com.github.andregpereira.resilientshop.productsapi.app.dto.produto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public record ProdutoDto(
    Long id,
    String nome,
    String descricao,
    BigDecimal valorUnitario,
    int estoque,
    BigDecimal rating,
    String imageUrl,
    @JsonFormat(shape = STRING, pattern = "dd/MM/yyyy HH:mm")
    LocalDateTime dataCriacao,
    @JsonFormat(shape = STRING, pattern = "dd/MM/yyyy HH:mm")
    LocalDateTime dataModificacao,
    boolean ativo
) {}
