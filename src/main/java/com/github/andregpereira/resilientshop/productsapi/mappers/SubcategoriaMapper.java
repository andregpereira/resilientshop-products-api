package com.github.andregpereira.resilientshop.productsapi.mappers;

import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Subcategoria;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubcategoriaMapper {

    Subcategoria toSubcategoria(SubcategoriaRegistroDto dto);
    SubcategoriaDto toSubcategoriaDto(Subcategoria subcategoria);

    SubcategoriaDetalhesDto toSubcategoriaDetalhesDto(Subcategoria subcategoria);

}
