package com.github.andregpereira.resilientshop.productsapi.app.services.subcategoria;

import com.github.andregpereira.resilientshop.productsapi.app.dto.subcategoria.SubcategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.subcategoria.SubcategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.SubcategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.SubcategoriaEntity;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.SubcategoriaRepository;
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

import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaConstants.SUBCATEGORIA;
import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaDtoConstants.SUBCATEGORIA_DETALHES_DTO;
import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaDtoConstants.SUBCATEGORIA_DTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SubcategoriaConsultaServiceTest {

    @InjectMocks
    private SubcategoriaConsultaServiceImpl consultaService;

    @Mock
    private SubcategoriaMapper mapper;

    @Mock
    private SubcategoriaRepository repository;

    @Test
    void listarSubcategoriasExistentesRetornaPageSubcategoriaDto() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        List<SubcategoriaEntity> listaSubcategorias = new ArrayList<>();
        listaSubcategorias.add(SUBCATEGORIA);
        Page<SubcategoriaEntity> pageSubcategorias = new PageImpl<>(listaSubcategorias, pageable, 10);
        given(repository.findAll(pageable)).willReturn(pageSubcategorias);
        given(mapper.toSubcategoriaDto(SUBCATEGORIA)).willReturn(SUBCATEGORIA_DTO);
        Page<SubcategoriaDto> sut = consultaService.listar(pageable);
        assertThat(sut).isNotEmpty().hasSize(1);
        assertThat(sut.getContent().get(0)).isEqualTo(SUBCATEGORIA_DTO);
    }

    @Test
    void listarSubcategoriasInexistentesThrowsException() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        given(repository.findAll(pageable)).willReturn(Page.empty());
        assertThatThrownBy(() -> consultaService.listar(pageable)).isInstanceOf(
                SubcategoriaNotFoundException.class).hasMessage("Poxa! Ainda não há subcategorias cadastradas");
    }

    @Test
    void consultarSubcategoriaPorIdExistenteRetornaSubcategoriaDetalhesDto() {
        given(repository.findById(1L)).willReturn(Optional.of(SUBCATEGORIA));
        given(mapper.toSubcategoriaDetalhesDto(SUBCATEGORIA)).willReturn(SUBCATEGORIA_DETALHES_DTO);
        SubcategoriaDetalhesDto sut = consultaService.consultarPorId(1L);
        assertThat(sut).isNotNull().isEqualTo(SUBCATEGORIA_DETALHES_DTO);
    }

    @Test
    void consultarSubcategoriaPorIdInexistenteThrowsException() {
        given(repository.findById(10L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> consultaService.consultarPorId(10L)).isInstanceOf(
                SubcategoriaNotFoundException.class).hasMessage("Ops! Nenhuma subcategoria foi encontrada com o id 10");
    }

}

