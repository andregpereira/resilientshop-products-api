package com.github.andregpereira.resilientshop.productsapi.constants;

import com.github.andregpereira.resilientshop.productsapi.infra.entities.SubcategoriaEntity;

import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaConstants.*;

public class SubcategoriaConstants {

    public static final SubcategoriaEntity SUBCATEGORIA = new SubcategoriaEntity(null, "nome", "Teste da classe Subcategoria",
            CATEGORIA);

    public static final SubcategoriaEntity SUBCATEGORIA_ATUALIZADA = new SubcategoriaEntity(null, "nome2",
            "Teste da classe Subcategoria2", CATEGORIA_ATUALIZADA);

    public static final SubcategoriaEntity SUBCATEGORIA_INVALIDA = new SubcategoriaEntity(null, "", "", CATEGORIA_INVALIDA);

    public static final SubcategoriaEntity SUBCATEGORIA_VAZIA = new SubcategoriaEntity();

}
