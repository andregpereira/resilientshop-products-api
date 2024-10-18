package com.github.andregpereira.resilientshop.productsapi.app.services.produto;

import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.ProdutoNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.ProdutoMapper;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.ProdutoEntity;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.ProdutoRepository;
import org.assertj.core.api.BDDAssertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Optional;

import static com.github.andregpereira.resilientshop.productsapi.util.constant.CommonConstants.PAGEABLE_ID;
import static com.github.andregpereira.resilientshop.productsapi.util.constant.CommonConstants.PAGEABLE_NOME;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.ProdutoMockFactory.getProdutoDetalhesDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.ProdutoMockFactory.getProdutoDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.ProdutoMockFactory.getProdutoEntity;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProdutoConsultaServiceTest {

    @InjectMocks
    private ProdutoConsultaServiceImpl produtoConsultaService;

    @Mock
    private ProdutoMapper mapper;

    @Mock
    private ProdutoRepository repository;

    @Test
    void listarProdutosExistentesRetornaPageProdutoDto() {
        final var listaProdutos = new ArrayList<ProdutoEntity>();
        listaProdutos.add(getProdutoEntity());
        listaProdutos.add(getProdutoEntity());
        final var pageProdutos = new PageImpl<>(listaProdutos, PAGEABLE_ID, 10);
        given(repository.findAll(any(Pageable.class))).willReturn(pageProdutos);
        given(mapper.toProdutoDto(any(ProdutoEntity.class))).willReturn(getProdutoDto());
//        given(mapper.toProdutoDto(any(ProdutoEntity.class))).willReturn(PRODUTO_DTO_ATUALIZADO);

        final var sut = produtoConsultaService.listar(PAGEABLE_ID);

        BDDAssertions
            .then(sut)
            .isNotEmpty()
            .containsExactlyInAnyOrder(getProdutoDto(), getProdutoDto());
        then(repository)
            .should()
            .findAll(any(Pageable.class));
        then(mapper)
            .should(times(2))
            .toProdutoDto(any(ProdutoEntity.class));
    }

    @Test
    void listarProdutosInexistentesRetornaEmpty() {
        given(repository.findAll(any(Pageable.class))).willReturn(Page.empty());

        final var sut = produtoConsultaService.listar(PAGEABLE_ID);

        BDDAssertions
            .then(sut)
            .isEmpty();
        then(repository)
            .should()
            .findAll(any(Pageable.class));
        then(mapper).shouldHaveNoInteractions();
    }

    @Test
    void consultarProdutoPorIdExistenteRetornaProdutoDetalhesDto() {
        final var dto = getProdutoDetalhesDto();
        given(repository.findById(anyLong())).willReturn(Optional.of(getProdutoEntity()));
        given(mapper.toProdutoDetalhesDto(any(ProdutoEntity.class))).willReturn(dto);

        final var sut = produtoConsultaService.consultarPorId(1L);

        BDDAssertions
            .then(sut)
            .isEqualTo(dto);
        then(repository)
            .should()
            .findById(anyLong());
        then(mapper)
            .should()
            .toProdutoDetalhesDto(any(ProdutoEntity.class));
    }

    @Test
    void consultarProdutoPorIdInexistenteThrowsException() {
        given(repository.findById(anyLong())).willReturn(Optional.empty());

        final ThrowingCallable sut = () -> produtoConsultaService.consultarPorId(10L);

        assertThatThrownBy(sut)
            .isInstanceOf(ProdutoNotFoundException.class)
            .hasMessage("Ops! Nenhum produto foi encontrado com o id 10");
        then(repository)
            .should()
            .findById(anyLong());
        then(mapper).shouldHaveNoInteractions();
    }

    @Test
    void consultarProdutoPorNomeExistenteRetornaProdutoDto() {
        final var listaProdutos = new ArrayList<ProdutoEntity>();
        listaProdutos.add(getProdutoEntity());
        listaProdutos.add(getProdutoEntity());
        final var pageProdutos = new PageImpl<>(listaProdutos, PAGEABLE_NOME, 10);
        given(repository.findByName(anyString(), any(Pageable.class))).willReturn(pageProdutos);
        given(mapper.toProdutoDto(any(ProdutoEntity.class))).willReturn(getProdutoDto());
//        given(mapper.toProdutoDto(any(ProdutoEntity.class))).willReturn(PRODUTO_DTO_ATUALIZADO);

        final var sut = produtoConsultaService.consultarPorNome("nome", PAGEABLE_NOME);

        BDDAssertions
            .then(sut)
            .isNotEmpty()
            .containsExactlyInAnyOrder(getProdutoDto(), getProdutoDto());
        then(repository)
            .should()
            .findByName(anyString(), any(Pageable.class));
        then(mapper)
            .should(times(2))
            .toProdutoDto(any(ProdutoEntity.class));
    }

    @Test
    void consultarProdutoPorNomeInexistenteRetornaEmpty() {
        given(repository.findByName(anyString(), any(Pageable.class))).willReturn(Page.empty());

        final var sut = produtoConsultaService.consultarPorNome("produto", PAGEABLE_NOME);

        BDDAssertions
            .then(sut)
            .isEmpty();
        then(repository)
            .should()
            .findByName(anyString(), any(Pageable.class));
        then(mapper).shouldHaveNoInteractions();
    }

    @Test
    void consultarProdutoPorNomeNuloThrowsException() {
        assertThatThrownBy(() -> produtoConsultaService.consultarPorNome(
            null,
            PAGEABLE_NOME
        )).isInstanceOf(RuntimeException.class);
    }

    @Test
    void consultarProdutoPorSubcategoriaExistenteRetornaProdutoDto() {
        final var listaProdutos = new ArrayList<ProdutoEntity>();
        listaProdutos.add(getProdutoEntity());
        final var pageProdutos = new PageImpl<>(listaProdutos, PAGEABLE_ID, 10);
        given(repository.findAllBySubcategoriaId(anyLong(), any(Pageable.class))).willReturn(pageProdutos);
        given(mapper.toProdutoDto(any(ProdutoEntity.class))).willReturn(getProdutoDto());

        final var sut = produtoConsultaService.consultarPorSubcategoria(1L, PAGEABLE_ID);

        BDDAssertions
            .then(sut)
            .isNotEmpty()
            .containsExactlyInAnyOrder(getProdutoDto());
        then(repository)
            .should()
            .findAllBySubcategoriaId(anyLong(), any(Pageable.class));
        then(mapper)
            .should()
            .toProdutoDto(any(ProdutoEntity.class));
    }

    @Test
    void consultarProdutoPorSubcategoriaInexistenteRetornaEmpty() {
        given(repository.findAllBySubcategoriaId(anyLong(), any(Pageable.class))).willReturn(Page.empty());

        final var sut = produtoConsultaService.consultarPorSubcategoria(1L, PAGEABLE_ID);

        BDDAssertions
            .then(sut)
            .isEmpty();
        then(repository)
            .should()
            .findAllBySubcategoriaId(anyLong(), any(Pageable.class));
        then(mapper).shouldHaveNoInteractions();
    }

    @Test
    void consultarProdutoPorCategoriaExistenteRetornaProdutoDto() {
        final var listaProdutos = new ArrayList<ProdutoEntity>();
        listaProdutos.add(getProdutoEntity());
        final var pageProdutos = new PageImpl<>(listaProdutos, PAGEABLE_ID, 10);
        given(repository.findAllByCategoriaId(anyLong(), any(Pageable.class))).willReturn(pageProdutos);
        given(mapper.toProdutoDto(any(ProdutoEntity.class))).willReturn(getProdutoDto());

        final var sut = produtoConsultaService.consultarPorCategoria(1L, PAGEABLE_ID);

        BDDAssertions
            .then(sut)
            .isNotEmpty()
            .containsExactlyInAnyOrder(getProdutoDto());
        then(repository)
            .should()
            .findAllByCategoriaId(anyLong(), any(Pageable.class));
        then(mapper)
            .should()
            .toProdutoDto(any(ProdutoEntity.class));
    }

}
