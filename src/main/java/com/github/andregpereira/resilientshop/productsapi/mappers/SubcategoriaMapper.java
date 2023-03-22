package com.github.andregpereira.resilientshop.productsapi.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Subcategoria;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SubcategoriaMapper {

	Subcategoria toSubcategoria(SubcategoriaDto dto);

	Subcategoria toSubcategoria(SubcategoriaRegistroDto dto);

	SubcategoriaDto toSubcategoriaDto(Subcategoria subcategoria);

}
