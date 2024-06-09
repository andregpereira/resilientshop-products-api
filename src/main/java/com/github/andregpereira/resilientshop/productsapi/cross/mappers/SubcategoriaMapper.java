package com.github.andregpereira.resilientshop.productsapi.cross.mappers;

import com.github.andregpereira.resilientshop.productsapi.app.dto.subcategoria.SubcategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.subcategoria.SubcategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.subcategoria.SubcategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.SubcategoriaEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

/**
 * Interface mapper de {@link SubcategoriaEntity}, {@link SubcategoriaDto} e {@link SubcategoriaDetalhesDto}.
 *
 * @author André Garcia
 * @see CategoriaMapper
 */
@Mapper(componentModel = ComponentModel.SPRING, uses = CategoriaMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubcategoriaMapper {

    SubcategoriaEntity toSubcategoria(SubcategoriaRegistroDto dto);

    SubcategoriaDto toSubcategoriaDto(SubcategoriaEntity subcategoria);

    SubcategoriaDetalhesDto toSubcategoriaDetalhesDto(SubcategoriaEntity subcategoria);

}
