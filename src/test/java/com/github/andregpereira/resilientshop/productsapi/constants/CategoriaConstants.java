package com.github.andregpereira.resilientshop.productsapi.constants;

import com.github.andregpereira.resilientshop.productsapi.infra.entities.CategoriaEntity;

public class CategoriaConstants {

    public static final CategoriaEntity CATEGORIA = new CategoriaEntity(null, "nome");

    public static final CategoriaEntity CATEGORIA_ATUALIZADA = new CategoriaEntity(null, "nome2");

    public static final CategoriaEntity CATEGORIA_INVALIDA = new CategoriaEntity(null, "");

    public static final CategoriaEntity CATEGORIA_VAZIA = new CategoriaEntity();

}
