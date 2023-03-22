package com.github.andregpereira.resilientshop.productsapi.constants;

import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaRegistroDto;

import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaDtoConstants.CATEGORIA_DTO;
import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaDtoConstants.CATEGORIA_DTO_ATUALIZADO;

public class SubcategoriaDtoConstants {

    public static final SubcategoriaDto SUBCATEGORIA_DTO = new SubcategoriaDto(null, "nome",
            "Teste da classe SubcategoriaDto");

    public static final SubcategoriaDto SUBCATEGORIA_DTO_ATUALIZADO = new SubcategoriaDto(null, "nome2",
            "Teste da classe SubcategoriaDto");

    public static final SubcategoriaDetalhesDto SUBCATEGORIA_DETALHES_DTO = new SubcategoriaDetalhesDto(null, "nome",
            "Teste da classe SubcategoriaDetalhesDto", CATEGORIA_DTO);

    public static final SubcategoriaDetalhesDto SUBCATEGORIA_DETALHES_DTO_ATUALIZADO = new SubcategoriaDetalhesDto(null,
            "nome2", "Teste da classe SubcategoriaDetalhesDto", CATEGORIA_DTO_ATUALIZADO);

    public static final SubcategoriaRegistroDto SUBCATEGORIA_REGISTRO_DTO = new SubcategoriaRegistroDto("nome",
            "Teste da classe SubcategoriaRegistroDto", 1L);

    public static final SubcategoriaRegistroDto SUBCATEGORIA_REGISTRO_DTO_ATUALIZADO = new SubcategoriaRegistroDto(
            "nome", "Teste da classe SubcategoriaRegistroDto", 2L);

    public static final SubcategoriaRegistroDto SUBCATEGORIA_REGISTRO_DTO_ATUALIZADO_INVALIDO = new SubcategoriaRegistroDto(
            "", "", null);

    public static final SubcategoriaRegistroDto SUBCATEGORIA_REGISTRO_DTO_INVALIDO = new SubcategoriaRegistroDto("", "",
            null);

}
