package com.github.andregpereira.resilientshop.productsapi.cross.mappers;

import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoAtualizacaoDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.CategoriaEntity;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.ProdutoEntity;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.SubcategoriaEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.CategoriaMockFactory.getCategoriaDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.ProdutoMockFactory.getProdutoAtualizacaoDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.ProdutoMockFactory.getProdutoDetalhesDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.ProdutoMockFactory.getProdutoDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.ProdutoMockFactory.getProdutoEntity;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.ProdutoMockFactory.getProdutoRegistroDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.SubcategoriaMockFactory.getSubcategoriaDto;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = ProdutoMapperImpl.class)
class ProdutoMapperTest {

    @InjectMocks
    private ProdutoMapperImpl produtoMapper;

    @Mock
    private SubcategoriaMapper subcategoriaMapper;

    @Mock
    private CategoriaMapper categoriaMapper;

    @Test
    void produtoRegistroDtoRetornaProduto() {
        then(produtoMapper.toProduto(getProdutoRegistroDto()))
            .isNotNull()
            .isExactlyInstanceOf(ProdutoEntity.class);
    }

    @Test
    void produtoAtualizacaoDtoRetornaProduto() {
        then(produtoMapper.toProduto(getProdutoAtualizacaoDto()))
            .isNotNull()
            .isExactlyInstanceOf(ProdutoEntity.class);
    }

    @Test
    void produtoRegistroDtoNuloRetornaNull() {
        then(produtoMapper.toProduto((ProdutoRegistroDto) null)).isNull();
    }

    @Test
    void produtoAtualizacaoDtoNuloRetornaNull() {
        then(produtoMapper.toProduto((ProdutoAtualizacaoDto) null)).isNull();
    }

    @Test
    void produtoRetornaProdutoDto() {
        then(produtoMapper.toProdutoDto(getProdutoEntity())).isEqualTo(getProdutoDto());
    }

    @Test
    void produtoRetornaProdutoDetalhesDto() {
        given(subcategoriaMapper.toSubcategoriaDto(any(SubcategoriaEntity.class))).willReturn(getSubcategoriaDto());
        given(categoriaMapper.toCategoriaDto(any(CategoriaEntity.class))).willReturn(getCategoriaDto());
        then(produtoMapper.toProdutoDetalhesDto(getProdutoEntity())).isEqualTo(getProdutoDetalhesDto());
    }

    @Test
    void produtoNuloRetornaProdutoDtoNull() {
        then(produtoMapper.toProdutoDto(null)).isNull();
    }

    @Test
    void produtoNuloRetornaProdutoDetalhesDtoNull() {
        then(produtoMapper.toProdutoDetalhesDto(null)).isNull();
    }

}
