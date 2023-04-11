package com.github.andregpereira.resilientshop.productsapi.services.subcategoria;

import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Subcategoria;
import com.github.andregpereira.resilientshop.productsapi.infra.exception.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.mappers.SubcategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.repositories.SubcategoriaRepository;
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

    @Spy
    private SubcategoriaMapper mapper = Mappers.getMapper(SubcategoriaMapper.class);

    @Mock
    private SubcategoriaRepository repository;

    @Test
    void listarSubcategoriasExistentesRetornaPageSubcategoriaDto() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        List<Subcategoria> listaSubcategorias = new ArrayList<>();
        listaSubcategorias.add(SUBCATEGORIA);
        Page<Subcategoria> pageSubcategorias = new PageImpl<>(listaSubcategorias, pageable, 10);
        given(repository.findAll(pageable)).willReturn(pageSubcategorias);
        Page<SubcategoriaDto> sut = consultaService.listar(pageable);
        assertThat(sut).isNotEmpty().hasSize(1);
        assertThat(sut.getContent().get(0)).isEqualTo(SUBCATEGORIA_DTO);
    }

    @Test
    void listarSubcategoriasInexistentesThrowsException() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        given(repository.findAll(pageable)).willReturn(Page.empty());
        assertThatThrownBy(() -> consultaService.listar(pageable)).isInstanceOf(
                SubcategoriaNotFoundException.class).hasMessage("Ops! Ainda não há subcategorias cadastradas");
    }

    @Test
    void consultarSubcategoriaPorIdExistenteRetornaSubcategoriaDetalhesDto() {
        given(repository.existsById(1L)).willReturn(true);
        given(repository.getReferenceById(1L)).willReturn(SUBCATEGORIA);
        SubcategoriaDetalhesDto sut = consultaService.consultarPorId(1L);
        assertThat(sut).isNotNull().isEqualTo(SUBCATEGORIA_DETALHES_DTO);
    }

    @Test
    void consultarSubcategoriaPorIdInexistenteThrowsException() {
        given(repository.existsById(10L)).willReturn(false);
        assertThatThrownBy(() -> consultaService.consultarPorId(10L)).isInstanceOf(
                SubcategoriaNotFoundException.class).hasMessage(
                "Desculpe, não foi possível encontrar uma subcategoria com o id 10. Verifique e tente novamente");
    }

}

