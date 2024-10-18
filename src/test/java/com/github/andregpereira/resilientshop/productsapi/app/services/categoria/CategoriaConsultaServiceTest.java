package com.github.andregpereira.resilientshop.productsapi.app.services.categoria;

import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.CategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.CategoriaEntity;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.CategoriaRepository;
import com.github.andregpereira.resilientshop.productsapi.util.mock.factory.CategoriaMockFactory;
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
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.CategoriaMockFactory.getCategoriaDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.CategoriaMockFactory.getCategoriaEntity;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.CategoriaMockFactory.getPageIdCategorias;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CategoriaConsultaServiceTest {

    @InjectMocks
    private CategoriaConsultaServiceImpl service;

    @Mock
    private CategoriaMapper mapper;

    @Mock
    private CategoriaRepository repository;

    @Test
    void consultarCategoriaPorIdExistenteRetornaCategoriaDto() {
        given(repository.findById(anyLong())).willReturn(Optional.of(getCategoriaEntity()));
        given(mapper.toCategoriaDto(any(CategoriaEntity.class))).willReturn(getCategoriaDto());

        final var sut = service.consultarPorId(1L);

        BDDAssertions
            .then(sut)
            .isNotNull()
            .isEqualTo(getCategoriaDto());
        then(repository)
            .should()
            .findById(anyLong());
        then(mapper)
            .should()
            .toCategoriaDto(any(CategoriaEntity.class));
    }

    @Test
    void consultarCategoriaPorIdInexistenteThrowsException() {
        given(repository.findById(anyLong())).willReturn(Optional.empty());

        final ThrowingCallable sut = () -> service.consultarPorId(10L);

        assertThatThrownBy(sut)
            .isInstanceOf(CategoriaNotFoundException.class)
            .hasMessage("Ops! Nenhuma categoria foi encontrada com o id 10");
        then(repository)
            .should()
            .findById(anyLong());
        then(mapper).shouldHaveNoInteractions();
    }

    @Test
    void listarCategoriasExistentesRetornaPageCategoriaDto() {
        final var listaCategorias = new ArrayList<CategoriaEntity>();
        listaCategorias.add(getCategoriaEntity());
        final var pageCategorias = new PageImpl<>(listaCategorias, PAGEABLE_ID, 10);
        given(repository.findAll(any(Pageable.class))).willReturn(pageCategorias);
        given(mapper.toCategoriaDto(any(CategoriaEntity.class))).willReturn(getCategoriaDto());

        final var sut = service.listar(PAGEABLE_ID);

        BDDAssertions
            .then(sut)
            .isNotEmpty()
            .containsExactlyInAnyOrder(getCategoriaDto());
        then(repository)
            .should()
            .findAll(any(Pageable.class));
        then(mapper)
            .should()
            .toCategoriaDto(any(CategoriaEntity.class));
    }

    @Test
    void listar_CategoriasInexistentes_RetornaPageVazia() {
        given(repository.findAll(any(Pageable.class))).willReturn(Page.empty());

        final var sut = service.listar(PAGEABLE_ID);

        BDDAssertions
            .then(sut)
            .isEmpty();
        then(repository)
            .should()
            .findAll(any(Pageable.class));
        then(mapper).shouldHaveNoInteractions();
    }

}

