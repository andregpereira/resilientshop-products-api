package com.github.andregpereira.resilientshop.productsapi.cross.mappers;

import com.github.andregpereira.resilientshop.productsapi.infra.entities.Subcategoria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaConstants.CATEGORIA;
import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaDtoConstants.CATEGORIA_DTO;
import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaConstants.SUBCATEGORIA;
import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaDtoConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = SubcategoriaMapperImpl.class)
class SubcategoriaMapperTest {

    @InjectMocks
    private SubcategoriaMapperImpl subcategoriaMapper;

    @Mock
    private CategoriaMapper categoriaMapper;

    @Test
    void subcategoriaRegistroDtoRetornaSubcategoria() {
        assertThat(subcategoriaMapper.toSubcategoria(SUBCATEGORIA_REGISTRO_DTO)).isNotNull().isExactlyInstanceOf(
                Subcategoria.class);
    }

    @Test
    void subcategoriaRegistroDtoNuloRetornaNull() {
        assertThat(subcategoriaMapper.toSubcategoria(null)).isNotEqualTo(SUBCATEGORIA);
    }

    @Test
    void subcategoriaRetornaSubcategoriaDto() {
        assertThat(subcategoriaMapper.toSubcategoriaDto(SUBCATEGORIA)).isEqualTo(SUBCATEGORIA_DTO);
    }

    @Test
    void subcategoriaRetornaSubcategoriaDetalhesDto() {
        given(categoriaMapper.toCategoriaDto(CATEGORIA)).willReturn(CATEGORIA_DTO);
        assertThat(subcategoriaMapper.toSubcategoriaDetalhesDto(SUBCATEGORIA)).isEqualTo(SUBCATEGORIA_DETALHES_DTO);
    }

    @Test
    void subcategoriaNuloRetornaSubcategoriaDtoNull() {
        assertThat(subcategoriaMapper.toSubcategoriaDto(null)).isNotEqualTo(SUBCATEGORIA_DTO);
    }

    @Test
    void subcategoriaNuloRetornaSubcategoriaDetalhesDtoNull() {
        assertThat(subcategoriaMapper.toSubcategoriaDetalhesDto(null)).isNotEqualTo(SUBCATEGORIA_DETALHES_DTO);
    }

}
