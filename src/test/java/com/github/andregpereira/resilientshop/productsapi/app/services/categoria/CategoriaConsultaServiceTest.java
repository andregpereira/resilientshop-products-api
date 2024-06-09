package com.github.andregpereira.resilientshop.productsapi.app.services.categoria;

import com.github.andregpereira.resilientshop.productsapi.app.dto.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.CategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.CategoriaEntity;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaConstants.CATEGORIA;
import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaDtoConstants.CATEGORIA_DTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CategoriaConsultaServiceTest {

    @InjectMocks
    private CategoriaConsultaServiceImpl consultaService;

    @Mock
    private CategoriaMapper mapper;

    @Mock
    private CategoriaRepository repository;

    @Test
    void consultarCategoriaPorIdExistenteRetornaCategoriaDto() {
        given(repository.findById(1L)).willReturn(Optional.of(CATEGORIA));
        given(mapper.toCategoriaDto(CATEGORIA)).willReturn(CATEGORIA_DTO);
        CategoriaDto sut = consultaService.consultarPorId(1L);
        assertThat(sut).isNotNull().isEqualTo(CATEGORIA_DTO);
        then(repository).should().findById(1L);
    }

    @Test
    void consultarCategoriaPorIdInexistenteThrowsException() {
        given(repository.findById(10L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> consultaService.consultarPorId(10L)).isInstanceOf(
                CategoriaNotFoundException.class).hasMessage("Ops! Nenhuma categoria foi encontrada com o id 10");
    }

    @Test
    void listarCategoriasExistentesRetornaPageCategoriaDto() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        List<CategoriaEntity> listaCategorias = new ArrayList<>();
        listaCategorias.add(CATEGORIA);
        Page<CategoriaEntity> pageCategorias = new PageImpl<>(listaCategorias, pageable, 10);
        given(repository.findAll(pageable)).willReturn(pageCategorias);
        given(mapper.toCategoriaDto(CATEGORIA)).willReturn(CATEGORIA_DTO);
        Page<CategoriaDto> sut = consultaService.listar(pageable);
        assertThat(sut).isNotEmpty().hasSize(1);
        assertThat(sut.getContent().get(0)).isEqualTo(CATEGORIA_DTO);
        then(repository).should().findAll(pageable);
    }

    @Test
    void listarCategoriasInexistentesThrowsException() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        given(repository.findAll(pageable)).willReturn(Page.empty());
        assertThatThrownBy(() -> consultaService.listar(pageable)).isInstanceOf(
                CategoriaNotFoundException.class).hasMessage("Poxa! Ainda não há categorias cadastradas");
    }

}

