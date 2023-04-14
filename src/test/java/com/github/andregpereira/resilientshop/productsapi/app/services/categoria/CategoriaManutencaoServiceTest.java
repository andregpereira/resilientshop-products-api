package com.github.andregpereira.resilientshop.productsapi.app.services.categoria;

import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.CategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.repositories.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaConstants.*;
import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaDtoConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class CategoriaManutencaoServiceTest {

    @InjectMocks
    private CategoriaManutencaoServiceImpl manutencaoService;

    @Spy
    private CategoriaMapper mapper = Mappers.getMapper(CategoriaMapper.class);

    @Mock
    private CategoriaRepository repository;

    @Test
    void criarCategoriaComDadosValidosRetornaCategoriaDto() {
        given(repository.existsByNome(CATEGORIA.getNome())).willReturn(false);
        given(repository.save(CATEGORIA)).willReturn(CATEGORIA);
        assertThat(manutencaoService.registrar(CATEGORIA_REGISTRO_DTO)).isEqualTo(CATEGORIA_DTO);
        then(repository).should().save(CATEGORIA);
    }

    @Test
    void criarCategoriaComDadosInvalidosThrowsException() {
        given(repository.existsByNome(CATEGORIA_INVALIDA.getNome())).willReturn(false);
        given(repository.save(CATEGORIA_INVALIDA)).willThrow(RuntimeException.class);
        assertThatThrownBy(() -> manutencaoService.registrar(CATEGORIA_REGISTRO_DTO_INVALIDA)).isInstanceOf(
                RuntimeException.class);
        then(repository).should(never()).save(CATEGORIA);
    }

    @Test
    void criarCategoriaComNomeExistenteThrowsException() {
        given(repository.existsByNome(CATEGORIA.getNome())).willReturn(true);
        assertThatThrownBy(() -> manutencaoService.registrar(CATEGORIA_REGISTRO_DTO)).isInstanceOf(
                CategoriaAlreadyExistsException.class);
        then(repository).should(never()).save(CATEGORIA);
    }

    @Test
    void atualizarCategoriaComDadosValidosRetornaCategoriaDto() {
        given(repository.existsById(CATEGORIA.getId())).willReturn(true);
        given(repository.existsByNome(CATEGORIA_ATUALIZADA.getNome())).willReturn(false);
        given(repository.save(CATEGORIA_ATUALIZADA)).willReturn(CATEGORIA_ATUALIZADA);
        assertThat(manutencaoService.atualizar(CATEGORIA.getId(), CATEGORIA_REGISTRO_DTO_ATUALIZADA)).isEqualTo(
                CATEGORIA_DTO_ATUALIZADA);
        then(repository).should().save(CATEGORIA_ATUALIZADA);
    }

    @Test
    void atualizarCategoriaComDadosInvalidosThrowsException() {
        assertThatThrownBy(() -> manutencaoService.atualizar(10L, CATEGORIA_REGISTRO_DTO_INVALIDA)).isInstanceOf(
                RuntimeException.class);
        then(repository).should(never()).save(CATEGORIA);
    }

    @Test
    void atualizarCategoriaComIdInexistenteThrowsException() {
        given(repository.existsById(10L)).willReturn(false);
        assertThatThrownBy(() -> manutencaoService.atualizar(10L, CATEGORIA_REGISTRO_DTO)).isInstanceOf(
                CategoriaNotFoundException.class);
        then(repository).should(never()).save(CATEGORIA);
    }

    @Test
    void atualizarCategoriaComNomeExistenteThrowsException() {
        given(repository.existsById(1L)).willReturn(true);
        given(repository.existsByNome(CATEGORIA.getNome())).willReturn(true);
        assertThatThrownBy(() -> manutencaoService.atualizar(1L, CATEGORIA_REGISTRO_DTO)).isInstanceOf(
                CategoriaAlreadyExistsException.class);
        then(repository).should(never()).save(CATEGORIA);
    }

    @Test
    void removerCategoriaComIdExistenteRetornaString() {
        given(repository.findById(1L)).willReturn(Optional.of(CATEGORIA));
        assertThat(manutencaoService.remover(1L)).isEqualTo("Categoria removida");
        then(repository).should().deleteById(1L);
    }

    @Test
    void removerCategoriaComIdInexistenteRetornaString() {
        given(repository.findById(10L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> manutencaoService.remover(10L)).isInstanceOf(
                CategoriaNotFoundException.class).hasMessage(
                "Desculpe, não foi possível encontrar uma categoria com o id 10. Verifique e tente novamente");
        then(repository).should(never()).deleteById(10L);
    }

}

