package com.github.andregpereira.resilientshop.productsapi.cross.mappers;

import com.github.andregpereira.resilientshop.productsapi.app.dtos.produto.ProdutoAtualizacaoDto;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.produto.ProdutoRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.Produto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import static com.github.andregpereira.resilientshop.productsapi.constants.ProdutoConstants.PRODUTO;
import static com.github.andregpereira.resilientshop.productsapi.constants.ProdutoDtoConstants.*;
import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaConstants.SUBCATEGORIA;
import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaDtoConstants.SUBCATEGORIA_DTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = ProdutoMapperImpl.class)
class ProdutoMapperTest {

    @InjectMocks
    private ProdutoMapperImpl produtoMapper;

    @Mock
    private SubcategoriaMapper subcategoriaMapper;

    @Test
    void produtoRegistroDtoRetornaProduto() {
        assertThat(produtoMapper.toProduto(PRODUTO_REGISTRO_DTO)).isNotNull().isExactlyInstanceOf(Produto.class);
    }

    @Test
    void produtoAtualizacaoDtoRetornaProduto() {
        assertThat(produtoMapper.toProduto(PRODUTO_ATUALIZACAO_DTO)).isNotNull().isExactlyInstanceOf(Produto.class);
    }

    @Test
    void produtoRegistroDtoNuloRetornaNull() {
        assertThat(produtoMapper.toProduto((ProdutoRegistroDto) null)).isNotEqualTo(PRODUTO);
    }

    @Test
    void produtoAtualizacaoDtoNuloRetornaNull() {
        assertThat(produtoMapper.toProduto((ProdutoAtualizacaoDto) null)).isNotEqualTo(PRODUTO);
    }

    @Test
    void produtoRetornaProdutoDto() {
        assertThat(produtoMapper.toProdutoDto(PRODUTO)).isEqualTo(PRODUTO_DTO);
    }

    @Test
    void produtoRetornaProdutoDetalhesDto() {
        given(subcategoriaMapper.toSubcategoriaDto(SUBCATEGORIA)).willReturn(SUBCATEGORIA_DTO);
        assertThat(produtoMapper.toProdutoDetalhesDto(PRODUTO)).isEqualTo(PRODUTO_DETALHES_DTO);
    }

    @Test
    void produtoNuloRetornaProdutoDtoNull() {
        assertThat(produtoMapper.toProdutoDto(null)).isNotEqualTo(PRODUTO_DTO);
    }

    @Test
    void produtoNuloRetornaProdutoDetalhesDtoNull() {
        assertThat(produtoMapper.toProdutoDetalhesDto(null)).isNotEqualTo(PRODUTO_DETALHES_DTO);
    }

}
