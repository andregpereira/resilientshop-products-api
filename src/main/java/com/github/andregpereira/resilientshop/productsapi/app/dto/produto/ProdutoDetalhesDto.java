package com.github.andregpereira.resilientshop.productsapi.app.dto.produto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.andregpereira.resilientshop.productsapi.app.dto.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.subcategoria.SubcategoriaDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public record ProdutoDetalhesDto(
    Long id,
    Long sku,
    String nome,
    String descricao,
    BigDecimal valorUnitario,
    int estoque,
    BigDecimal rating,
    String imageUrl,
    @JsonFormat(shape = STRING, pattern = "dd/MM/uuuu HH:mm")
    LocalDateTime dataCriacao,
    @JsonFormat(shape = STRING, pattern = "dd/MM/uuuu HH:mm")
    LocalDateTime dataModificacao,
    boolean ativo,
    CategoriaDto categoria,
    SubcategoriaDto subcategoria
) {}
