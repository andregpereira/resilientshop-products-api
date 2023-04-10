package com.github.andregpereira.resilientshop.productsapi.constants;

import com.github.andregpereira.resilientshop.productsapi.entities.Subcategoria;

import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaConstants.*;

public class SubcategoriaConstants {

    public static final Subcategoria SUBCATEGORIA = new Subcategoria(null, "nome", "Teste da classe Subcategoria",
            CATEGORIA);

    public static final Subcategoria SUBCATEGORIA_ATUALIZADA = new Subcategoria(null, "nome2",
            "Teste da classe Subcategoria2", CATEGORIA_ATUALIZADA);

    public static final Subcategoria SUBCATEGORIA_INVALIDA = new Subcategoria(null, "", "", CATEGORIA_INVALIDA);

}
