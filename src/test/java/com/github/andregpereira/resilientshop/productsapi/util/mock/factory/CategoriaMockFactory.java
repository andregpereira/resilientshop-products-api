package com.github.andregpereira.resilientshop.productsapi.util.mock.factory;

import com.github.andregpereira.resilientshop.productsapi.app.dto.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.categoria.CategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.CategoriaEntity;
import com.github.andregpereira.resilientshop.productsapi.util.constant.CommonConstants;
import com.github.andregpereira.resilientshop.productsapi.util.json.JsonReader;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static com.github.andregpereira.resilientshop.productsapi.util.constant.CommonConstants.PAGEABLE_ID;
import static com.github.andregpereira.resilientshop.productsapi.util.constant.CommonConstants.PAGEABLE_NOME;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.InvalidObjectMockFactory.readInvalidJson;

@UtilityClass
public class CategoriaMockFactory {

    private static final String MOCKS_PATH = CommonConstants.MOCKS_PATH.concat("/categoria");

    private static <T> T readJson(final Class<T> clazz) {
        return JsonReader.read(MOCKS_PATH.concat("/categoria.json"), clazz);
    }

    public static CategoriaDto getCategoriaDto() {
        return readJson(CategoriaDto.class);
    }

    public static CategoriaRegistroDto getCategoriaRegistroDto() {
        return readJson(CategoriaRegistroDto.class);
    }

    public static Object getCategoriaInvalida() {
        return readInvalidJson();
    }

    public static CategoriaEntity getCategoriaEntity() {
        return readJson(CategoriaEntity.class);
    }

    public static CategoriaEntity getCategoriaEntityInvalida() {
        return readInvalidJson(CategoriaEntity.class);
    }

    public static Page<CategoriaDto> getPageIdCategorias() {
        return new PageImpl<>(getListaCategorias(), PAGEABLE_ID, 10);
    }

    public static Page<CategoriaDto> getPageNomeCategorias() {
        return new PageImpl<>(getListaCategorias(), PAGEABLE_NOME, 10);
    }

    private static List<CategoriaDto> getListaCategorias() {
        return List.of(getCategoriaDto(), getCategoriaDto());
    }

}
