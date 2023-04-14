package com.github.andregpereira.resilientshop.productsapi.mappers;

import com.github.andregpereira.resilientshop.productsapi.app.dtos.produto.ProdutoAtualizacaoDto;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.produto.ProdutoRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.ProdutoMapperImpl;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.Produto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.andregpereira.resilientshop.productsapi.constants.ProdutoConstants.PRODUTO;
import static com.github.andregpereira.resilientshop.productsapi.constants.ProdutoDtoConstants.*;
import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaConstants.SUBCATEGORIA;
import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaDtoConstants.SUBCATEGORIA_DTO;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ProdutoMapperImpl.class)
class ProdutoMapperTest {

    @InjectMocks
    private ProdutoMapperImpl mapper;

    @Test
    void produtoRegistroDtoRetornaProduto() {
        assertThat(mapper.toProduto(PRODUTO_REGISTRO_DTO)).isNotNull().isExactlyInstanceOf(Produto.class);
    }

    @Test
    void produtoAtualizacaoDtoRetornaProduto() {
        assertThat(mapper.toProduto(PRODUTO_ATUALIZACAO_DTO)).isNotNull().isExactlyInstanceOf(Produto.class);
    }

    @Test
    void produtoRegistroDtoNuloRetornaNull() {
        assertThat(mapper.toProduto((ProdutoRegistroDto) null)).isNotEqualTo(PRODUTO);
    }

    @Test
    void produtoAtualizacaoDtoNuloRetornaNull() {
        assertThat(mapper.toProduto((ProdutoAtualizacaoDto) null)).isNotEqualTo(PRODUTO);
    }

    @Test
    void produtoRetornaProdutoDto() {
        assertThat(mapper.toProdutoDto(PRODUTO)).isEqualTo(PRODUTO_DTO);
    }

    @Test
    void produtoRetornaProdutoDetalhesDto() {
        assertThat(mapper.toProdutoDetalhesDto(PRODUTO)).isEqualTo(PRODUTO_DETALHES_DTO);
    }

    @Test
    void produtoNuloRetornaProdutoDtoNull() {
        assertThat(mapper.toProdutoDto(null)).isNotEqualTo(PRODUTO_DTO);
    }

    @Test
    void produtoNuloRetornaProdutoDetalhesDtoNull() {
        assertThat(mapper.toProdutoDetalhesDto(null)).isNotEqualTo(PRODUTO_DETALHES_DTO);
    }

    @Test
    void subcategoriaRetornaSubcategoriaDto() {
        assertThat(mapper.subcategoriaToSubcategoriaDto(SUBCATEGORIA)).isEqualTo(SUBCATEGORIA_DTO);
    }

    @Test
    void subcategoriaNulaRetornaNull() {
        assertThat(mapper.subcategoriaToSubcategoriaDto(null)).isNotEqualTo(SUBCATEGORIA_DTO);
    }

}
