package com.github.andregpereira.resilientshop.productsapi.cross.mappers;

import com.github.andregpereira.resilientshop.productsapi.app.dtos.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.categoria.CategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

/**
 * Interface mapper de {@link Categoria} e {@link CategoriaDto}.
 *
 * @author André Garcia
 * @see CategoriaMapper
 */
@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoriaMapper {

    Categoria toCategoria(CategoriaRegistroDto dto);

    CategoriaDto toCategoriaDto(Categoria categoria);

}
