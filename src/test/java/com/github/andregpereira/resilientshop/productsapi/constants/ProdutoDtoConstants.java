package com.github.andregpereira.resilientshop.productsapi.constants;

import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoAtualizacaoDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoRegistroDto;

import java.math.BigDecimal;

import static com.github.andregpereira.resilientshop.productsapi.constants.ProdutoConstants.LOCAL_DATE_TIME;
import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaDtoConstants.SUBCATEGORIA_DTO;
import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaDtoConstants.SUBCATEGORIA_DTO_ATUALIZADO;

public class ProdutoDtoConstants {

    public static final ProdutoDto PRODUTO_DTO = new ProdutoDto(null, "nome", "Teste da classe ProdutoDto",
            LOCAL_DATE_TIME, BigDecimal.valueOf(10.59), 10);

    public static final ProdutoDto PRODUTO_DTO_ATUALIZADO = new ProdutoDto(null, "nome2", "Teste da classe ProdutoDto2",
            LOCAL_DATE_TIME, BigDecimal.valueOf(19.99), 87);

    public static final ProdutoDetalhesDto PRODUTO_DETALHES_DTO = new ProdutoDetalhesDto(null, 123456789L, "nome",
            "Teste da classe ProdutoDetalhesDto", LOCAL_DATE_TIME, BigDecimal.valueOf(10.59), 10, SUBCATEGORIA_DTO);

    public static final ProdutoDetalhesDto PRODUTO_DETALHES_DTO_ATUALIZADO = new ProdutoDetalhesDto(null, 123456789L,
            "nome2", "Teste da classe ProdutoAtualizacaoDto", LOCAL_DATE_TIME, BigDecimal.valueOf(29.99), 8,
            SUBCATEGORIA_DTO_ATUALIZADO);

    public static final ProdutoRegistroDto PRODUTO_REGISTRO_DTO = new ProdutoRegistroDto(123456789L, "nome",
            "Teste da classe ProdutoRegistroDto", BigDecimal.valueOf(10.59), 10, 1L);

    public static final ProdutoRegistroDto PRODUTO_REGISTRO_DTO_INVALIDO = new ProdutoRegistroDto(null, "", "", null, 0,
            null);

    public static final ProdutoAtualizacaoDto PRODUTO_ATUALIZACAO_DTO = new ProdutoAtualizacaoDto("nome2",
            "Teste da classe ProdutoAtualizacaoDto", BigDecimal.valueOf(29.99), 8, 2L);

    public static final ProdutoAtualizacaoDto PRODUTO_ATUALIZACAO_DTO_INVALIDO = new ProdutoAtualizacaoDto("", "", null,
            0, null);

}
