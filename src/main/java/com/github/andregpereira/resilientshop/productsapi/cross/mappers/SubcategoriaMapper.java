package com.github.andregpereira.resilientshop.productsapi.cross.mappers;

import com.github.andregpereira.resilientshop.productsapi.app.dtos.subcategoria.SubcategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.subcategoria.SubcategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.subcategoria.SubcategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.Subcategoria;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ComponentModel.SPRING, uses = CategoriaMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubcategoriaMapper {

    Subcategoria toSubcategoria(SubcategoriaRegistroDto dto);

    SubcategoriaDto toSubcategoriaDto(Subcategoria subcategoria);

    SubcategoriaDetalhesDto toSubcategoriaDetalhesDto(Subcategoria subcategoria);

}
