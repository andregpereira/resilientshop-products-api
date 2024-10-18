package com.github.andregpereira.resilientshop.productsapi.app.services.subcategoria;

import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.SubcategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.SubcategoriaEntity;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.SubcategoriaRepository;
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
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.SubcategoriaMockFactory.getSubcategoriaDetalhesDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.SubcategoriaMockFactory.getSubcategoriaDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.SubcategoriaMockFactory.getSubcategoriaEntity;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class SubcategoriaConsultaServiceTest {

    @InjectMocks
    private SubcategoriaConsultaServiceImpl service;

    @Mock
    private SubcategoriaMapper mapper;

    @Mock
    private SubcategoriaRepository repository;

    @Test
    void listarSubcategoriasExistentesRetornaPageSubcategoriaDto() {
        final var listaSubcategorias = new ArrayList<SubcategoriaEntity>();
        listaSubcategorias.add(getSubcategoriaEntity());
        final var pageSubcategorias = new PageImpl<>(listaSubcategorias, PAGEABLE_ID, 10);
        given(repository.findAll(any(Pageable.class))).willReturn(pageSubcategorias);
        given(mapper.toSubcategoriaDto(any(SubcategoriaEntity.class))).willReturn(getSubcategoriaDto());

        final var sut = service.listar(PAGEABLE_ID);

        BDDAssertions
            .then(sut)
            .isNotEmpty()
            .containsExactlyInAnyOrder(getSubcategoriaDto());
        then(repository)
            .should()
            .findAll(any(Pageable.class));
        then(mapper)
            .should()
            .toSubcategoriaDto(any(SubcategoriaEntity.class));
    }

    @Test
    void listarSubcategoriasInexistentesRetornaPageEmpty() {
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

    @Test
    void consultarSubcategoriaPorIdExistenteRetornaSubcategoriaDetalhesDto() {
        given(repository.findById(anyLong())).willReturn(Optional.of(getSubcategoriaEntity()));
        given(mapper.toSubcategoriaDetalhesDto(any(SubcategoriaEntity.class))).willReturn(getSubcategoriaDetalhesDto());

        final var sut = service.consultarPorId(1L);

        BDDAssertions
            .then(sut)
            .isNotNull()
            .isEqualTo(getSubcategoriaDetalhesDto());
        then(repository)
            .should()
            .findById(anyLong());
        then(mapper)
            .should()
            .toSubcategoriaDetalhesDto(any(SubcategoriaEntity.class));
    }

    @Test
    void consultarSubcategoriaPorIdInexistenteThrowsException() {
        given(repository.findById(anyLong())).willReturn(Optional.empty());

        final ThrowingCallable sut = () -> service.consultarPorId(10L);

        assertThatThrownBy(sut)
            .isInstanceOf(SubcategoriaNotFoundException.class)
            .hasMessage("Ops! Nenhuma subcategoria foi encontrada com o id 10");
        then(repository)
            .should()
            .findById(anyLong());
        then(mapper).shouldHaveNoInteractions();
    }

}

