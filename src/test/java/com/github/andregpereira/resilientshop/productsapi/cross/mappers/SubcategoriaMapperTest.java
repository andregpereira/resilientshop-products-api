package com.github.andregpereira.resilientshop.productsapi.mappers;

import com.github.andregpereira.resilientshop.productsapi.cross.mappers.SubcategoriaMapperImpl;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.Subcategoria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaConstants.CATEGORIA;
import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaDtoConstants.CATEGORIA_DTO;
import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaConstants.SUBCATEGORIA;
import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaDtoConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SubcategoriaMapperImpl.class)
class SubcategoriaMapperTest {

    @InjectMocks
    private SubcategoriaMapperImpl mapper;

    @Test
    void subcategoriaRegistroDtoRetornaSubcategoria() {
        assertThat(mapper.toSubcategoria(SUBCATEGORIA_REGISTRO_DTO)).isNotNull().isExactlyInstanceOf(
                Subcategoria.class);
    }

    @Test
    void subcategoriaRegistroDtoNuloRetornaNull() {
        assertThat(mapper.toSubcategoria(null)).isNotEqualTo(SUBCATEGORIA);
    }

    @Test
    void subcategoriaRetornaSubcategoriaDto() {
        assertThat(mapper.toSubcategoriaDto(SUBCATEGORIA)).isEqualTo(SUBCATEGORIA_DTO);
    }

    @Test
    void subcategoriaRetornaSubcategoriaDetalhesDto() {
        assertThat(mapper.toSubcategoriaDetalhesDto(SUBCATEGORIA)).isEqualTo(SUBCATEGORIA_DETALHES_DTO);
    }

    @Test
    void subcategoriaNuloRetornaSubcategoriaDtoNull() {
        assertThat(mapper.toSubcategoriaDto(null)).isNotEqualTo(SUBCATEGORIA_DTO);
    }

    @Test
    void subcategoriaNuloRetornaSubcategoriaDetalhesDtoNull() {
        assertThat(mapper.toSubcategoriaDetalhesDto(null)).isNotEqualTo(SUBCATEGORIA_DETALHES_DTO);
    }

    @Test
    void categoriaRetornaCategoriaDto() {
        assertThat(mapper.categoriaToCategoriaDto(CATEGORIA)).isEqualTo(CATEGORIA_DTO);
    }

    @Test
    void categoriaNuloRetornaNull() {
        assertThat(mapper.categoriaToCategoriaDto(null)).isNotEqualTo(CATEGORIA_DTO);
    }

}
