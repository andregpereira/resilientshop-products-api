package com.github.andregpereira.resilientshop.productsapi.cross.mappers;

import com.github.andregpereira.resilientshop.productsapi.app.dto.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.categoria.CategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.CategoriaEntity;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Interface mapper de {@link CategoriaEntity} e {@link CategoriaDto}.
 *
 * @author André Garcia
 * @see CategoriaMapper
 */
@Mapper(componentModel = SPRING, unmappedTargetPolicy = IGNORE)
public interface CategoriaMapper {

    CategoriaEntity toCategoria(CategoriaRegistroDto dto);

    CategoriaDto toCategoriaDto(CategoriaEntity categoria);

}
