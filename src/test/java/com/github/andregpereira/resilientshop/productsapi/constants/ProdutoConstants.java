package com.github.andregpereira.resilientshop.productsapi.constants;

import com.github.andregpereira.resilientshop.productsapi.infra.entities.Produto;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaConstants.*;

public class ProdutoConstants {

    public static final LocalDateTime LOCAL_DATE_TIME_FIXADO = LocalDateTime.now(
            Clock.fixed(Instant.parse("2014-12-22T10:15:30.00Z"), ZoneId.systemDefault()));

    public static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();

    public static final Produto PRODUTO = new Produto(null, 123456789L, "nome", "Teste da classe Produto",
            LOCAL_DATE_TIME, BigDecimal.valueOf(10.59), 10, SUBCATEGORIA);

    public static final Produto PRODUTO_LOCAL_DATE_TIME_FIXADO = new Produto(null, 123456789L, "nome",
            "Teste da classe Produto", LOCAL_DATE_TIME_FIXADO, BigDecimal.valueOf(10.59), 10, SUBCATEGORIA);

    public static final Produto PRODUTO_ATUALIZADO = new Produto(null, 123456789L, "nome2", "Teste da classe Produto2",
            LOCAL_DATE_TIME, BigDecimal.valueOf(29.99), 87, SUBCATEGORIA_ATUALIZADA);

    public static final Produto PRODUTO_INVALIDO = new Produto(null, null, "", "", null, null, 0,
            SUBCATEGORIA_INVALIDA);

    public static final Produto PRODUTO_VAZIO = new Produto();

}
