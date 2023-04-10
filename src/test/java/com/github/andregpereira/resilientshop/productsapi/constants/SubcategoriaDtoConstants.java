package com.github.andregpereira.resilientshop.productsapi.constants;

import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaRegistroDto;

import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaDtoConstants.CATEGORIA_DTO;
import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaDtoConstants.CATEGORIA_DTO_ATUALIZADA;

public class SubcategoriaDtoConstants {

    public static final SubcategoriaDto SUBCATEGORIA_DTO = new SubcategoriaDto(null, "nome",
            "Teste da classe Subcategoria");

    public static final SubcategoriaDto SUBCATEGORIA_DTO_ATUALIZADA = new SubcategoriaDto(null, "nome2",
            "Teste da classe Subcategoria2");

    public static final SubcategoriaDetalhesDto SUBCATEGORIA_DETALHES_DTO = new SubcategoriaDetalhesDto(null, "nome",
            "Teste da classe Subcategoria", CATEGORIA_DTO);

    public static final SubcategoriaDetalhesDto SUBCATEGORIA_DETALHES_DTO_ATUALIZADA = new SubcategoriaDetalhesDto(null,
            "nome2", "Teste da classe Subcategoria2", CATEGORIA_DTO_ATUALIZADA);

    public static final SubcategoriaRegistroDto SUBCATEGORIA_REGISTRO_DTO = new SubcategoriaRegistroDto("nome",
            "Teste da classe Subcategoria", 1L);

    public static final SubcategoriaRegistroDto SUBCATEGORIA_REGISTRO_DTO_ATUALIZADA = new SubcategoriaRegistroDto(
            "nome2", "Teste da classe Subcategoria2", 2L);

    public static final SubcategoriaRegistroDto SUBCATEGORIA_REGISTRO_DTO_ATUALIZADO_INVALIDA = new SubcategoriaRegistroDto(
            "", "", null);

    public static final SubcategoriaRegistroDto SUBCATEGORIA_REGISTRO_DTO_INVALIDA = new SubcategoriaRegistroDto("", "",
            null);

}
