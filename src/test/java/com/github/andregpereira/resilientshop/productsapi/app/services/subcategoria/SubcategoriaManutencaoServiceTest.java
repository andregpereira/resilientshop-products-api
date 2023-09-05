package com.github.andregpereira.resilientshop.productsapi.app.services.subcategoria;

import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.SubcategoriaAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.SubcategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.CategoriaRepository;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.SubcategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.MessageFormat;
import java.util.Optional;

import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaConstants.CATEGORIA;
import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaConstants.CATEGORIA_ATUALIZADA;
import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaConstants.SUBCATEGORIA;
import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaConstants.SUBCATEGORIA_ATUALIZADA;
import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaDtoConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class SubcategoriaManutencaoServiceTest {

    @InjectMocks
    private SubcategoriaManutencaoServiceImpl manutencaoService;

    @Mock
    private SubcategoriaMapper mapper;

    @Mock
    private SubcategoriaRepository subcategoriaRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Test
    void criarSubcategoriaComDadosValidosRetornaSubcategoriaDetalhesDto() {
        given(subcategoriaRepository.existsByNome(SUBCATEGORIA_REGISTRO_DTO.nome())).willReturn(false);
        given(categoriaRepository.findById(1L)).willReturn(Optional.of(CATEGORIA));
        given(mapper.toSubcategoria(SUBCATEGORIA_REGISTRO_DTO)).willReturn(SUBCATEGORIA);
        given(subcategoriaRepository.save(SUBCATEGORIA)).willReturn(SUBCATEGORIA);
        given(mapper.toSubcategoriaDetalhesDto(SUBCATEGORIA)).willReturn(SUBCATEGORIA_DETALHES_DTO);
        assertThat(manutencaoService.criar(SUBCATEGORIA_REGISTRO_DTO)).isEqualTo(SUBCATEGORIA_DETALHES_DTO);
        then(subcategoriaRepository).should().save(SUBCATEGORIA);
    }

    @Test
    void criarSubcategoriaComDadosInvalidosThrowsException() {
        assertThatThrownBy(() -> manutencaoService.criar(SUBCATEGORIA_REGISTRO_DTO_INVALIDA)).isInstanceOf(
                RuntimeException.class);
        then(subcategoriaRepository).should(never()).save(SUBCATEGORIA);
    }

    @Test
    void criarSubcategoriaComNomeExistenteThrowsException() {
        given(subcategoriaRepository.existsByNome(SUBCATEGORIA_REGISTRO_DTO.nome())).willReturn(true);
        assertThatThrownBy(() -> manutencaoService.criar(SUBCATEGORIA_REGISTRO_DTO)).isInstanceOf(
                SubcategoriaAlreadyExistsException.class).hasMessage(
                MessageFormat.format("Poxa! Já existe uma subcategoria cadastrada com o nome {0}",
                        SUBCATEGORIA_REGISTRO_DTO.nome()));
        then(subcategoriaRepository).should(never()).save(SUBCATEGORIA);
    }

    @Test
    void criarSubcategoriaComIdCategoriaInexistenteThrowsException() {
        given(subcategoriaRepository.existsByNome(SUBCATEGORIA_REGISTRO_DTO.nome())).willReturn(false);
        given(categoriaRepository.findById(1L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> manutencaoService.criar(SUBCATEGORIA_REGISTRO_DTO)).isInstanceOf(
                CategoriaNotFoundException.class).hasMessage("Ops! Nenhuma categoria foi encontrada com o id 1");
        then(subcategoriaRepository).should(never()).save(SUBCATEGORIA);
    }

    @Test
    void atualizarSubcategoriaComDadosValidosRetornaSubcategoriaDetalhesDto() {
        given(subcategoriaRepository.findById(1L)).willReturn(Optional.of(SUBCATEGORIA));
        given(subcategoriaRepository.existsByNome(SUBCATEGORIA_REGISTRO_DTO_ATUALIZADA.nome())).willReturn(false);
        given(categoriaRepository.findById(SUBCATEGORIA_REGISTRO_DTO_ATUALIZADA.categoriaId())).willReturn(
                Optional.of(CATEGORIA_ATUALIZADA));
        given(mapper.toSubcategoria(SUBCATEGORIA_REGISTRO_DTO_ATUALIZADA)).willReturn(SUBCATEGORIA_ATUALIZADA);
        given(subcategoriaRepository.save(SUBCATEGORIA_ATUALIZADA)).willReturn(SUBCATEGORIA_ATUALIZADA);
        given(mapper.toSubcategoriaDetalhesDto(SUBCATEGORIA_ATUALIZADA)).willReturn(
                SUBCATEGORIA_DETALHES_DTO_ATUALIZADA);
        assertThat(manutencaoService.atualizar(1L, SUBCATEGORIA_REGISTRO_DTO_ATUALIZADA)).isEqualTo(
                SUBCATEGORIA_DETALHES_DTO_ATUALIZADA);
        then(subcategoriaRepository).should().save(SUBCATEGORIA_ATUALIZADA);
    }

    @Test
    void atualizarSubcategoriaComDadosInvalidosThrowsException() {
        assertThatThrownBy(
                () -> manutencaoService.atualizar(10L, SUBCATEGORIA_REGISTRO_DTO_ATUALIZADO_INVALIDA)).isInstanceOf(
                RuntimeException.class);
        then(subcategoriaRepository).should(never()).save(SUBCATEGORIA_ATUALIZADA);
    }

    @Test
    void atualizarSubcategoriaComIdInexistenteThrowsException() {
        given(subcategoriaRepository.findById(10L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> manutencaoService.atualizar(10L, SUBCATEGORIA_REGISTRO_DTO_ATUALIZADA)).isInstanceOf(
                SubcategoriaNotFoundException.class).hasMessage("Ops! Nenhuma subcategoria foi encontrada com o id 10");
        then(subcategoriaRepository).should(never()).save(SUBCATEGORIA_ATUALIZADA);
    }

    @Test
    void atualizarSubcategoriaComNomeExistenteThrowsException() {
        given(subcategoriaRepository.findById(1L)).willReturn(Optional.of(SUBCATEGORIA));
        given(subcategoriaRepository.existsByNome(SUBCATEGORIA_REGISTRO_DTO_ATUALIZADA.nome())).willReturn(true);
        assertThatThrownBy(() -> manutencaoService.atualizar(1L, SUBCATEGORIA_REGISTRO_DTO_ATUALIZADA)).isInstanceOf(
                SubcategoriaAlreadyExistsException.class).hasMessage(
                MessageFormat.format("Poxa! Já existe uma subcategoria cadastrada com o nome {0}",
                        SUBCATEGORIA_REGISTRO_DTO_ATUALIZADA.nome()));
        then(subcategoriaRepository).should(never()).save(SUBCATEGORIA_ATUALIZADA);
    }

    @Test
    void atualizarSubcategoriaComIdICategoriaInexistenteThrowsException() {
        given(subcategoriaRepository.findById(1L)).willReturn(Optional.of(SUBCATEGORIA_ATUALIZADA));
        given(subcategoriaRepository.existsByNome(SUBCATEGORIA_REGISTRO_DTO_ATUALIZADA.nome())).willReturn(false);
        given(categoriaRepository.findById(2L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> manutencaoService.atualizar(1L, SUBCATEGORIA_REGISTRO_DTO_ATUALIZADA)).isInstanceOf(
                CategoriaNotFoundException.class).hasMessage("Ops! Nenhuma categoria foi encontrada com o id 2");
        then(subcategoriaRepository).should(never()).save(SUBCATEGORIA_ATUALIZADA);
    }

    @Test
    void removerSubcategoriaComIdExistenteRetornaString() {
        given(subcategoriaRepository.findById(1L)).willReturn(Optional.of(SUBCATEGORIA));
        assertThat(manutencaoService.remover(1L)).isEqualTo("Subcategoria com id 1 removida com sucesso");
        then(subcategoriaRepository).should().deleteById(1L);
    }

    @Test
    void removerSubcategoriaComIdInexistenteThrowsException() {
        given(subcategoriaRepository.findById(10L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> manutencaoService.remover(10L)).isInstanceOf(
                SubcategoriaNotFoundException.class).hasMessage("Ops! Nenhuma subcategoria foi encontrada com o id 10");
        then(subcategoriaRepository).should(never()).deleteById(10L);
    }

}

