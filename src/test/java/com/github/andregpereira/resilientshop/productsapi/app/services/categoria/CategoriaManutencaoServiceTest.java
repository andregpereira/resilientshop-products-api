package com.github.andregpereira.resilientshop.productsapi.app.services.categoria;

import com.github.andregpereira.resilientshop.productsapi.app.dto.categoria.CategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.CategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.CategoriaEntity;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.CategoriaRepository;
import org.assertj.core.api.BDDAssertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.CategoriaMockFactory.getCategoriaDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.CategoriaMockFactory.getCategoriaEntity;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.CategoriaMockFactory.getCategoriaRegistroDto;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class CategoriaManutencaoServiceTest {

    @InjectMocks
    private CategoriaManutencaoServiceImpl service;

    @Mock
    private CategoriaMapper mapper;

    @Mock
    private CategoriaRepository repository;

    @Test
    void criarCategoriaComDadosValidosRetornaCategoriaDto() {
        given(repository.existsByNome(anyString())).willReturn(false);
        given(mapper.toCategoria(any(CategoriaRegistroDto.class))).willReturn(getCategoriaEntity());
        given(repository.save(any(CategoriaEntity.class))).willReturn(getCategoriaEntity());
        given(mapper.toCategoriaDto(any(CategoriaEntity.class))).willReturn(getCategoriaDto());

        final var sut = service.criar(getCategoriaRegistroDto());

        BDDAssertions
            .then(sut)
            .isNotNull()
            .isEqualTo(getCategoriaDto());
        then(repository)
            .should()
            .existsByNome(anyString());
        then(mapper)
            .should()
            .toCategoria(any(CategoriaRegistroDto.class));
        then(repository)
            .should()
            .save(any(CategoriaEntity.class));
        then(mapper)
            .should()
            .toCategoriaDto(any(CategoriaEntity.class));
    }

    @Test
    void criarCategoriaComDadosInvalidosThrowsException() {
        given(repository.existsByNome(anyString())).willReturn(false);
        given(mapper.toCategoria(any(CategoriaRegistroDto.class))).willReturn(getCategoriaEntity());
        given(repository.save(any(CategoriaEntity.class))).willThrow(RuntimeException.class);

        final ThrowingCallable sut = () -> service.criar(getCategoriaRegistroDto());

        assertThatThrownBy(sut).isInstanceOf(RuntimeException.class);
        then(repository)
            .should()
            .existsByNome(anyString());
        then(mapper)
            .should()
            .toCategoria(any(CategoriaRegistroDto.class));
        then(repository)
            .should()
            .save(any(CategoriaEntity.class));
        then(mapper).shouldHaveNoMoreInteractions();
    }

    @Test
    void criarCategoriaComNomeExistenteThrowsException() {
        given(repository.existsByNome(anyString())).willReturn(true);

        final ThrowingCallable sut = () -> service.criar(getCategoriaRegistroDto());

        assertThatThrownBy(sut).isInstanceOf(CategoriaAlreadyExistsException.class);
        then(repository)
            .should()
            .existsByNome(anyString());
        then(repository).shouldHaveNoMoreInteractions();
        then(mapper).shouldHaveNoInteractions();
    }

    @Test
    void atualizarCategoriaComDadosValidosRetornaCategoriaDto() {
        given(repository.findById(anyLong())).willReturn(Optional.of(getCategoriaEntity()));
        given(mapper.toCategoria(any(CategoriaRegistroDto.class))).willReturn(getCategoriaEntity());
        given(repository.existsByNome(anyString())).willReturn(false);
        given(repository.save(any(CategoriaEntity.class))).willReturn(getCategoriaEntity());
        given(mapper.toCategoriaDto(any(CategoriaEntity.class))).willReturn(getCategoriaDto());

        final var sut = service.atualizar(1L, getCategoriaRegistroDto());

        BDDAssertions
            .then(sut)
            .isEqualTo(getCategoriaDto());
        then(repository)
            .should()
            .findById(anyLong());
        then(mapper)
            .should()
            .toCategoria(any(CategoriaRegistroDto.class));
        then(repository)
            .should()
            .existsByNome(anyString());
        then(repository)
            .should()
            .save(any(CategoriaEntity.class));
        then(mapper)
            .should()
            .toCategoriaDto(any(CategoriaEntity.class));
    }

    @Test
    void atualizarCategoriaComDadosInvalidosThrowsException() {
        given(repository.findById(anyLong())).willReturn(Optional.of(getCategoriaEntity()));
        given(mapper.toCategoria(any(CategoriaRegistroDto.class))).willReturn(getCategoriaEntity());
        given(repository.existsByNome(anyString())).willReturn(false);
        given(repository.save(any(CategoriaEntity.class))).willThrow(RuntimeException.class);

        final ThrowingCallable sut = () -> service.atualizar(10L, getCategoriaRegistroDto());

        assertThatThrownBy(sut).isExactlyInstanceOf(RuntimeException.class);
        then(repository)
            .should()
            .findById(anyLong());
        then(mapper)
            .should()
            .toCategoria(any(CategoriaRegistroDto.class));
        then(repository)
            .should()
            .existsByNome(anyString());
        then(repository)
            .should()
            .save(any(CategoriaEntity.class));
        then(mapper).shouldHaveNoMoreInteractions();
    }

    @Test
    void atualizarCategoriaComIdInexistenteThrowsException() {
        given(repository.findById(anyLong())).willReturn(Optional.empty());

        final ThrowingCallable sut = () -> service.atualizar(10L, getCategoriaRegistroDto());

        assertThatThrownBy(sut)
            .isInstanceOf(CategoriaNotFoundException.class)
            .hasMessage("Ops! Nenhuma categoria foi encontrada com o id 10");
        then(repository)
            .should()
            .findById(anyLong());
        then(repository).shouldHaveNoMoreInteractions();
        then(mapper).shouldHaveNoInteractions();
    }

    @Test
    void atualizarCategoriaComNomeExistenteThrowsException() {
        given(repository.findById(anyLong())).willReturn(Optional.of(getCategoriaEntity()));
        given(repository.existsByNome(anyString())).willReturn(true);

        final ThrowingCallable sut = () -> service.atualizar(1L, getCategoriaRegistroDto());

        assertThatThrownBy(sut)
            .isInstanceOf(CategoriaAlreadyExistsException.class)
            .hasMessage("Opa! Já existe uma categoria cadastrada com o nome nome");
        then(repository)
            .should()
            .findById(anyLong());
        then(repository)
            .should()
            .existsByNome(anyString());
        then(repository).shouldHaveNoMoreInteractions();
        then(mapper).shouldHaveNoInteractions();
    }

    @Test
    void removerCategoriaComIdExistenteRetornaString() {
        given(repository.findById(anyLong())).willReturn(Optional.of(getCategoriaEntity()));

        final var sut = service.remover(1L);

        BDDAssertions
            .then(sut)
            .isEqualTo("Categoria com id 1 removida com sucesso");
        then(repository)
            .should()
            .deleteById(anyLong());
    }

    @Test
    void removerCategoriaComIdInexistenteRetornaString() {
        given(repository.findById(anyLong())).willReturn(Optional.empty());

        final ThrowingCallable sut = () -> service.remover(10L);

        assertThatThrownBy(sut)
            .isInstanceOf(CategoriaNotFoundException.class)
            .hasMessage("Ops! Nenhuma categoria foi encontrada com o id 10");
        then(repository)
            .should(never())
            .deleteById(anyLong());
    }

}
