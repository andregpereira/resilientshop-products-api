package com.github.andregpereira.resilientshop.productsapi.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Categoria;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoriaMapper {

	Categoria toCategoria(CategoriaDto dto);

	Categoria toCategoria(CategoriaRegistroDto dto);

	CategoriaDto toCategoriaDto(Categoria categoria);

	CategoriaDetalhesDto toCategoriaDetalhesDto(Categoria categoria);

}
