package com.github.andregpereira.resilientshop.productsapi.cross.mappers;

import com.github.andregpereira.resilientshop.productsapi.app.dto.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.categoria.CategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.CategoriaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

/**
 * Interface mapper de {@link CategoriaEntity} e {@link CategoriaDto}.
 *
 * @author André Garcia
 * @see CategoriaMapper
 */
@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoriaMapper {

    CategoriaEntity toCategoria(CategoriaRegistroDto dto);

    CategoriaDto toCategoriaDto(CategoriaEntity categoria);

}
