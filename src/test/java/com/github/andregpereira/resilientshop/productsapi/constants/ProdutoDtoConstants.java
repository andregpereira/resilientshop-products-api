package com.github.andregpereira.resilientshop.productsapi.constants;

import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoAtualizacaoDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoRegistroDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaDtoConstants.CATEGORIA_DTO;
import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaDtoConstants.CATEGORIA_DTO_ATUALIZADA;
import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaDtoConstants.SUBCATEGORIA_DTO;
import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaDtoConstants.SUBCATEGORIA_DTO_ATUALIZADA;

public class ProdutoDtoConstants {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm");

    public static final LocalDateTime DATA_CRIACAO = LocalDateTime.parse("24/09/2021 16:31", DATE_TIME_FORMATTER);

    public static final LocalDateTime DATA_MODIFICACAO = LocalDateTime.parse("12/07/2023 11:54", DATE_TIME_FORMATTER);

    public static final Pageable PAGEABLE_ID = PageRequest.of(0, 10, Direction.ASC, "id");

    public static final Pageable PAGEABLE_NOME = PageRequest.of(0, 10, Direction.ASC, "nome");

    public static final long SKU = 123456789L;

    public static final String NOME = "nome";

    public static final String DESCRICAO = "teste da classe Produto";

    public static final BigDecimal VALOR_UNITARIO = BigDecimal.valueOf(10.59);

    public static final int ESTOQUE = 10;

    public static final BigDecimal RATING = BigDecimal.valueOf(3.6);

    public static final String IMAGE_URL = "https://";

    public static final boolean ATIVO_TRUE = true;

    public static final ProdutoDto PRODUTO_DTO = new ProdutoDto(null,
            NOME,
            DESCRICAO,
            VALOR_UNITARIO,
            ESTOQUE,
            RATING,
            IMAGE_URL,
            DATA_CRIACAO,
            DATA_MODIFICACAO,
            ATIVO_TRUE);

    public static final ProdutoDetalhesDto PRODUTO_DETALHES_DTO = new ProdutoDetalhesDto(null,
            SKU,
            NOME,
            DESCRICAO,
            VALOR_UNITARIO,
            ESTOQUE,
            RATING,
            IMAGE_URL,
            DATA_CRIACAO,
            DATA_MODIFICACAO,
            ATIVO_TRUE,
            CATEGORIA_DTO,
            SUBCATEGORIA_DTO);

    public static final String NOME_ATUALIZADO = "nome2";

    public static final String DESCRICAO_ATUALIZADA = "teste da classe Produto2";

    public static final BigDecimal VALOR_UNITARIO_ATUALIZADO = BigDecimal.valueOf(29.99);

    public static final int ESTOQUE_ATUALIZADO = 87;

    public static final BigDecimal RATING_ATUALIZADO = BigDecimal.valueOf(4.3);

    public static final ProdutoDto PRODUTO_DTO_ATUALIZADO = new ProdutoDto(null,
            NOME_ATUALIZADO,
            DESCRICAO_ATUALIZADA,
            VALOR_UNITARIO_ATUALIZADO,
            ESTOQUE_ATUALIZADO,
            RATING_ATUALIZADO,
            IMAGE_URL,
            DATA_CRIACAO,
            DATA_MODIFICACAO,
            ATIVO_TRUE);

    public static final List<ProdutoDto> LISTA_PRODUTOS = List.of(PRODUTO_DTO, PRODUTO_DTO_ATUALIZADO);

    public static final Page<ProdutoDto> PAGE_ID_PRODUTOS = new PageImpl<>(LISTA_PRODUTOS, PAGEABLE_ID, 10);

    public static final Page<ProdutoDto> PAGE_NOME_PRODUTOS = new PageImpl<>(LISTA_PRODUTOS, PAGEABLE_NOME, 10);

    public static final ProdutoDetalhesDto PRODUTO_DETALHES_DTO_ATUALIZADO = new ProdutoDetalhesDto(null,
            SKU,
            NOME_ATUALIZADO,
            DESCRICAO_ATUALIZADA,
            VALOR_UNITARIO_ATUALIZADO,
            ESTOQUE_ATUALIZADO,
            RATING_ATUALIZADO,
            IMAGE_URL,
            DATA_CRIACAO,
            DATA_MODIFICACAO,
            ATIVO_TRUE,
            CATEGORIA_DTO_ATUALIZADA,
            SUBCATEGORIA_DTO_ATUALIZADA);

    public static final long ID_CATEGORIA_SUBCATEGORIA = 1L;

    public static final ProdutoRegistroDto PRODUTO_REGISTRO_DTO = new ProdutoRegistroDto(SKU,
            NOME,
            DESCRICAO,
            VALOR_UNITARIO,
            ESTOQUE,
            IMAGE_URL,
            ID_CATEGORIA_SUBCATEGORIA,
            ID_CATEGORIA_SUBCATEGORIA);

    public static final ProdutoRegistroDto PRODUTO_REGISTRO_DTO_INVALIDO =
            new ProdutoRegistroDto(null, "", "", BigDecimal.ZERO, 0, "", null, null);

    public static final long ID_CATEGORIA_SUBCATEGORIA_ATUALIZADO = 10L;

    public static final ProdutoAtualizacaoDto PRODUTO_ATUALIZACAO_DTO = new ProdutoAtualizacaoDto(NOME_ATUALIZADO,
            DESCRICAO_ATUALIZADA,
            VALOR_UNITARIO_ATUALIZADO,
            ESTOQUE_ATUALIZADO,
            IMAGE_URL,
            ATIVO_TRUE,
            ID_CATEGORIA_SUBCATEGORIA_ATUALIZADO,
            ID_CATEGORIA_SUBCATEGORIA_ATUALIZADO);

    public static final ProdutoAtualizacaoDto PRODUTO_ATUALIZACAO_DTO_INVALIDO =
            new ProdutoAtualizacaoDto("", "", null, 0, "", false, null, null);

}
