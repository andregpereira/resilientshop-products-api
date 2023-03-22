package com.github.andregpereira.resilientshop.productsapi.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoAtualizacaoDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Produto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProdutoMapper {

	Produto toProduto(ProdutoDto dto);

	Produto toProduto(ProdutoRegistroDto dto);

	Produto toProduto(ProdutoAtualizacaoDto dto);

	ProdutoDto toProdutoDto(Produto produto);

	ProdutoDetalhesDto toProdutoDetalhesDto(Produto produto);

}
