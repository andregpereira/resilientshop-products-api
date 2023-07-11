package com.github.andregpereira.resilientshop.productsapi.constants;

import com.github.andregpereira.resilientshop.productsapi.infra.entities.Categoria;

public class CategoriaConstants {

    public static final Categoria CATEGORIA = new Categoria(null, "nome");

    public static final Categoria CATEGORIA_ATUALIZADA = new Categoria(null, "nome2");

    public static final Categoria CATEGORIA_INVALIDA = new Categoria(null, "");

    public static final Categoria CATEGORIA_VAZIA = new Categoria();

}
