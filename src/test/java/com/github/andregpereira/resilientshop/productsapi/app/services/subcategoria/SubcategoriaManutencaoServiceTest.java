package com.github.andregpereira.resilientshop.productsapi.app.services.subcategoria;

import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.SubcategoriaAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.SubcategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.CategoriaRepository;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.SubcategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Spy
    private SubcategoriaMapper mapper = Mappers.getMapper(SubcategoriaMapper.class);

    @Mock
    private SubcategoriaRepository subcategoriaRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Test
    void criarSubcategoriaComDadosValidosRetornaSubcategoriaDetalhesDto() {
        given(categoriaRepository.existsById(1L)).willReturn(true);
        given(categoriaRepository.getReferenceById(1L)).willReturn(CATEGORIA);
        given(subcategoriaRepository.existsByNome(SUBCATEGORIA.getNome())).willReturn(false);
        given(subcategoriaRepository.save(SUBCATEGORIA)).willReturn(SUBCATEGORIA);
        assertThat(manutencaoService.registrar(SUBCATEGORIA_REGISTRO_DTO)).isEqualTo(SUBCATEGORIA_DETALHES_DTO);
        then(subcategoriaRepository).should().save(SUBCATEGORIA);
    }

    @Test
    void criarSubcategoriaComDadosInvalidosThrowsException() {
        assertThatThrownBy(() -> manutencaoService.registrar(SUBCATEGORIA_REGISTRO_DTO_INVALIDA)).isInstanceOf(
                RuntimeException.class);
        then(subcategoriaRepository).should(never()).save(SUBCATEGORIA);
    }

    @Test
    void criarSubcategoriaComIdCategoriaInexistenteThrowsException() {
        given(categoriaRepository.existsById(1L)).willReturn(false);
        assertThatThrownBy(() -> manutencaoService.registrar(SUBCATEGORIA_REGISTRO_DTO)).isInstanceOf(
                CategoriaNotFoundException.class).hasMessage("Ops! Não foi encontrada uma categoria com o id 1");
        then(subcategoriaRepository).should(never()).save(SUBCATEGORIA);
    }

    @Test
    void criarSubcategoriaComNomeExistenteThrowsException() {
        given(categoriaRepository.existsById(1L)).willReturn(true);
        given(subcategoriaRepository.existsByNome(SUBCATEGORIA.getNome())).willReturn(true);
        assertThatThrownBy(() -> manutencaoService.registrar(SUBCATEGORIA_REGISTRO_DTO)).isInstanceOf(
                SubcategoriaAlreadyExistsException.class);
        then(subcategoriaRepository).should(never()).save(SUBCATEGORIA);
    }

    @Test
    void atualizarSubcategoriaComDadosValidosRetornaSubcategoriaDetalhesDto() {
        given(subcategoriaRepository.existsById(SUBCATEGORIA.getId())).willReturn(true);
        given(categoriaRepository.existsById(SUBCATEGORIA_REGISTRO_DTO_ATUALIZADA.idCategoria())).willReturn(true);
        given(subcategoriaRepository.existsByNome(SUBCATEGORIA_ATUALIZADA.getNome())).willReturn(false);
        given(categoriaRepository.getReferenceById(SUBCATEGORIA_REGISTRO_DTO_ATUALIZADA.idCategoria())).willReturn(
                CATEGORIA_ATUALIZADA);
        given(subcategoriaRepository.save(SUBCATEGORIA_ATUALIZADA)).willReturn(SUBCATEGORIA_ATUALIZADA);
        assertThat(manutencaoService.atualizar(SUBCATEGORIA.getId(), SUBCATEGORIA_REGISTRO_DTO_ATUALIZADA)).isEqualTo(
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
        given(subcategoriaRepository.existsById(10L)).willReturn(false);
        assertThatThrownBy(() -> manutencaoService.atualizar(10L, SUBCATEGORIA_REGISTRO_DTO_ATUALIZADA)).isInstanceOf(
                SubcategoriaNotFoundException.class);
        then(subcategoriaRepository).should(never()).save(SUBCATEGORIA_ATUALIZADA);
    }

    @Test
    void atualizarSubcategoriaComIdICategoriaInexistenteThrowsException() {
        given(subcategoriaRepository.existsById(10L)).willReturn(true);
        given(categoriaRepository.existsById(2L)).willReturn(false);
        assertThatThrownBy(() -> manutencaoService.atualizar(10L, SUBCATEGORIA_REGISTRO_DTO_ATUALIZADA)).isInstanceOf(
                CategoriaNotFoundException.class);
        then(subcategoriaRepository).should(never()).save(SUBCATEGORIA_ATUALIZADA);
    }

    @Test
    void atualizarSubcategoriaComNomeExistenteThrowsException() {
        given(categoriaRepository.existsById(SUBCATEGORIA_REGISTRO_DTO_ATUALIZADA.idCategoria())).willReturn(true);
        given(subcategoriaRepository.existsById(1L)).willReturn(true);
        given(subcategoriaRepository.existsByNome(SUBCATEGORIA_ATUALIZADA.getNome())).willReturn(true);
        assertThatThrownBy(() -> manutencaoService.atualizar(1L, SUBCATEGORIA_REGISTRO_DTO_ATUALIZADA)).isInstanceOf(
                SubcategoriaAlreadyExistsException.class);
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
                SubcategoriaNotFoundException.class).hasMessage(
                "Ops! Não foi possível encontrar uma subcategoria com o id 10");
        then(subcategoriaRepository).should(never()).deleteById(10L);
    }

}

