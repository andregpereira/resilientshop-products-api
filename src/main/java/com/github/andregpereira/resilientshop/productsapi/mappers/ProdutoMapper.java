package com.github.andregpereira.resilientshop.productsapi.mappers;

import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoAtualizacaoDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProdutoMapper {

    Produto toProduto(ProdutoRegistroDto dto);

    Produto toProduto(ProdutoAtualizacaoDto dto);
    ProdutoDto toProdutoDto(Produto produto);

    ProdutoDetalhesDto toProdutoDetalhesDto(Produto produto);

}
