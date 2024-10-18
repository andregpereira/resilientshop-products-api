package com.github.andregpereira.resilientshop.productsapi.cross.mappers;

import com.github.andregpereira.resilientshop.productsapi.app.dto.subcategoria.SubcategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.subcategoria.SubcategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.subcategoria.SubcategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.SubcategoriaEntity;
import org.mapstruct.Mapper;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Interface mapper de {@link SubcategoriaEntity}, {@link SubcategoriaDto} e {@link SubcategoriaDetalhesDto}.
 *
 * @author André Garcia
 * @see CategoriaMapper
 */
@Mapper(
    componentModel = SPRING,
    uses = CategoriaMapper.class,
    injectionStrategy = CONSTRUCTOR,
    unmappedTargetPolicy = IGNORE
)
public interface SubcategoriaMapper {

    SubcategoriaEntity toSubcategoria(SubcategoriaRegistroDto dto);

    SubcategoriaDto toSubcategoriaDto(SubcategoriaEntity subcategoria);

    SubcategoriaDetalhesDto toSubcategoriaDetalhesDto(SubcategoriaEntity subcategoria);

}
