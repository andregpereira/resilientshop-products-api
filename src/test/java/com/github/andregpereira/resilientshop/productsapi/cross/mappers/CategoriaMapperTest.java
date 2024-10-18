package com.github.andregpereira.resilientshop.productsapi.cross.mappers;

import com.github.andregpereira.resilientshop.productsapi.infra.entities.CategoriaEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.CategoriaMockFactory.getCategoriaDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.CategoriaMockFactory.getCategoriaEntity;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.CategoriaMockFactory.getCategoriaRegistroDto;
import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CategoriaMapperImpl.class)
class CategoriaMapperTest {

    @Autowired
    private CategoriaMapperImpl mapper;

    @Test
    void categoriaRegistroDtoRetornaCategoria() {
        then(mapper.toCategoria(getCategoriaRegistroDto()))
            .isNotNull()
            .isExactlyInstanceOf(CategoriaEntity.class);
    }

    @Test
    void categoriaRegistroDtoNuloRetornaNull() {
        then(mapper.toCategoria(null)).isNull();
    }

    @Test
    void categoriaRetornaCategoriaDto() {
        then(mapper.toCategoriaDto(getCategoriaEntity())).isEqualTo(getCategoriaDto());
    }

    @Test
    void categoriaNuloRetornaCategoriaDtoNull() {
        then(mapper.toCategoriaDto(null)).isNull();
    }

}
