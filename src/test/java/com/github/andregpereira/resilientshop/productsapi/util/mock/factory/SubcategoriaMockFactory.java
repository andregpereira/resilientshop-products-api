package com.github.andregpereira.resilientshop.productsapi.util.mock.factory;

import com.github.andregpereira.resilientshop.productsapi.app.dto.subcategoria.SubcategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.subcategoria.SubcategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.subcategoria.SubcategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.SubcategoriaEntity;
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
public class SubcategoriaMockFactory {

    private static final String MOCKS_PATH = CommonConstants.MOCKS_PATH.concat("/subcategoria");

    private static <T> T readJson(final String filePath, final Class<T> clazz) {
        return JsonReader.read(MOCKS_PATH.concat(filePath), clazz);
    }

    public static SubcategoriaDto getSubcategoriaDto() {
        return readJson("/subcategoria.json", SubcategoriaDto.class);
    }

    public static SubcategoriaDetalhesDto getSubcategoriaDetalhesDto() {
        return readJson("/subcategoria.json", SubcategoriaDetalhesDto.class);
    }

    public static SubcategoriaRegistroDto getSubcategoriaRegistroDto() {
        return readJson("/subcategoria-registro.json", SubcategoriaRegistroDto.class);
    }

    public static Object getSubcategoriaInvalida() {
        return readInvalidJson();
    }

    public static SubcategoriaEntity getSubcategoriaEntity() {
        return readJson("/subcategoria.json", SubcategoriaEntity.class);
    }

    public static SubcategoriaEntity getSubcategoriaEntityInvalida() {
        return readInvalidJson(SubcategoriaEntity.class);
    }

    public static Page<SubcategoriaDto> getPageIdSubcategorias() {
        return new PageImpl<>(getListaSubcategorias(), PAGEABLE_ID, 10);
    }

    public static Page<SubcategoriaDto> getPageNomeSubcategorias() {
        return new PageImpl<>(getListaSubcategorias(), PAGEABLE_NOME, 10);
    }

    private static List<SubcategoriaDto> getListaSubcategorias() {
        return List.of(getSubcategoriaDto(), getSubcategoriaDto());
    }

}
