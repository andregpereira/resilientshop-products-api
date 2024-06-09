package com.github.andregpereira.resilientshop.productsapi.cross.mappers;

import com.github.andregpereira.resilientshop.productsapi.infra.entities.CategoriaEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaConstants.CATEGORIA;
import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaDtoConstants.CATEGORIA_DTO;
import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaDtoConstants.CATEGORIA_REGISTRO_DTO;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CategoriaMapperImpl.class)
class CategoriaMapperTest {

    @InjectMocks
    private CategoriaMapperImpl mapper;

    @Test
    void categoriaRegistroDtoRetornaCategoria() {
        assertThat(mapper.toCategoria(CATEGORIA_REGISTRO_DTO)).isNotNull().isExactlyInstanceOf(CategoriaEntity.class);
    }

    @Test
    void categoriaRegistroDtoNuloRetornaNull() {
        assertThat(mapper.toCategoria(null)).isNotEqualTo(CATEGORIA);
    }

    @Test
    void categoriaRetornaCategoriaDto() {
        assertThat(mapper.toCategoriaDto(CATEGORIA)).isEqualTo(CATEGORIA_DTO);
    }

    @Test
    void categoriaNuloRetornaCategoriaDtoNull() {
        assertThat(mapper.toCategoriaDto(null)).isNotEqualTo(CATEGORIA_DTO);
    }

}
