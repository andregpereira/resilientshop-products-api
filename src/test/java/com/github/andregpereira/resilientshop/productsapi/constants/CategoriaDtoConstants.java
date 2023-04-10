package com.github.andregpereira.resilientshop.productsapi.constants;

import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaRegistroDto;

public class CategoriaDtoConstants {

    public static final CategoriaDto CATEGORIA_DTO = new CategoriaDto(null, "nome");

    public static final CategoriaDto CATEGORIA_DTO_ATUALIZADO = new CategoriaDto(null, "nome2");

    public static final CategoriaRegistroDto CATEGORIA_REGISTRO_DTO = new CategoriaRegistroDto("nome");

    public static final CategoriaRegistroDto CATEGORIA_REGISTRO_DTO_ATUALIZADO = new CategoriaRegistroDto("nome2");

    public static final CategoriaRegistroDto CATEGORIA_REGISTRO_DTO_INVALIDO = new CategoriaRegistroDto("");

}
