package com.github.andregpereira.resilientshop.productsapi.constants;

import com.github.andregpereira.resilientshop.productsapi.entities.Produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaConstants.SUBCATEGORIA;

public class ProdutoConstants {

    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy - HH:mm")
    public static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();

    public static final Produto PRODUTO = new Produto(null, 123456789L, "nome", "Teste da classe Produto",
            LOCAL_DATE_TIME, BigDecimal.valueOf(10.59), 10, SUBCATEGORIA);

}
