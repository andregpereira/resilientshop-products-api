package com.github.andregpereira.resilientshop.productsapi.constants;

import com.github.andregpereira.resilientshop.productsapi.app.dtos.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.categoria.CategoriaRegistroDto;

public class CategoriaDtoConstants {

    public static final CategoriaDto CATEGORIA_DTO = new CategoriaDto(null, "nome");

    public static final CategoriaDto CATEGORIA_DTO_ATUALIZADA = new CategoriaDto(null, "nome2");

    public static final CategoriaRegistroDto CATEGORIA_REGISTRO_DTO = new CategoriaRegistroDto("nome");

    public static final CategoriaRegistroDto CATEGORIA_REGISTRO_DTO_ATUALIZADA = new CategoriaRegistroDto("nome2");

    public static final CategoriaRegistroDto CATEGORIA_REGISTRO_DTO_INVALIDA = new CategoriaRegistroDto("");

}
