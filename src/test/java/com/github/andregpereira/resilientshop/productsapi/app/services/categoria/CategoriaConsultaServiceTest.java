package com.github.andregpereira.resilientshop.productsapi.app.services.categoria;

import com.github.andregpereira.resilientshop.productsapi.app.dtos.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.Categoria;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.CategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaConstants.CATEGORIA;
import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaDtoConstants.CATEGORIA_DTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CategoriaConsultaServiceTest {

    @InjectMocks
    private CategoriaConsultaServiceImpl consultaService;

    @Spy
    private CategoriaMapper mapper = Mappers.getMapper(CategoriaMapper.class);

    @Mock
    private CategoriaRepository repository;

    @Test
    void consultarCategoriaPorIdExistenteRetornaCategoriaDto() {
        given(repository.existsById(1L)).willReturn(true);
        given(repository.getReferenceById(1L)).willReturn(CATEGORIA);
        CategoriaDto sut = consultaService.consultarPorId(1L);
        assertThat(sut).isNotNull().isEqualTo(CATEGORIA_DTO);
    }

    @Test
    void consultarCategoriaPorIdInexistenteThrowsException() {
        given(repository.existsById(10L)).willReturn(false);
        assertThatThrownBy(() -> consultaService.consultarPorId(10L)).isInstanceOf(
                CategoriaNotFoundException.class).hasMessage(
                "Desculpe, não foi possível encontrar uma categoria com o id 10. Verifique e tente novamente");
    }

    @Test
    void listarCategoriasExistentesRetornaPageCategoriaDto() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        List<Categoria> listaCategorias = new ArrayList<>();
        listaCategorias.add(CATEGORIA);
        Page<Categoria> pageCategorias = new PageImpl<>(listaCategorias, pageable, 10);
        given(repository.findAll(pageable)).willReturn(pageCategorias);
        Page<CategoriaDto> sut = consultaService.listar(pageable);
        assertThat(sut).isNotEmpty().hasSize(1);
        assertThat(sut.getContent().get(0)).isEqualTo(CATEGORIA_DTO);
    }

    @Test
    void listarCategoriasInexistentesThrowsException() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        given(repository.findAll(pageable)).willReturn(Page.empty());
        assertThatThrownBy(() -> consultaService.listar(pageable)).isInstanceOf(
                CategoriaNotFoundException.class).hasMessage("Ops! Ainda não há categorias cadastradas");
    }

}

