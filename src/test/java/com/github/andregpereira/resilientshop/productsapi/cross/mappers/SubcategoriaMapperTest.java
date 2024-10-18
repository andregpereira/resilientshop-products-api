package com.github.andregpereira.resilientshop.productsapi.cross.mappers;

import com.github.andregpereira.resilientshop.productsapi.infra.entities.CategoriaEntity;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.SubcategoriaEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.CategoriaMockFactory.getCategoriaDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.SubcategoriaMockFactory.getSubcategoriaDetalhesDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.SubcategoriaMockFactory.getSubcategoriaDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.SubcategoriaMockFactory.getSubcategoriaEntity;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.SubcategoriaMockFactory.getSubcategoriaRegistroDto;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
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
        then(subcategoriaMapper.toSubcategoria(getSubcategoriaRegistroDto()))
            .isNotNull()
            .isExactlyInstanceOf(SubcategoriaEntity.class);
    }

    @Test
    void subcategoriaRegistroDtoNuloRetornaNull() {
        then(subcategoriaMapper.toSubcategoria(null)).isNull();
    }

    @Test
    void subcategoriaRetornaSubcategoriaDto() {
        then(subcategoriaMapper.toSubcategoriaDto(getSubcategoriaEntity())).isEqualTo(getSubcategoriaDto());
    }

    @Test
    void subcategoriaRetornaSubcategoriaDetalhesDto() {
        given(categoriaMapper.toCategoriaDto(any(CategoriaEntity.class))).willReturn(getCategoriaDto());
        then(subcategoriaMapper.toSubcategoriaDetalhesDto(getSubcategoriaEntity())).isEqualTo(getSubcategoriaDetalhesDto());
    }

    @Test
    void subcategoriaNuloRetornaSubcategoriaDtoNull() {
        then(subcategoriaMapper.toSubcategoriaDto(null)).isNull();
    }

    @Test
    void subcategoriaNuloRetornaSubcategoriaDetalhesDtoNull() {
        then(subcategoriaMapper.toSubcategoriaDetalhesDto(null)).isNull();
    }

}
