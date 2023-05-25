package com.github.andregpereira.resilientshop.productsapi.cross.mappers;

import com.github.andregpereira.resilientshop.productsapi.app.dtos.produto.ProdutoAtualizacaoDto;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.produto.ProdutoDto;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.produto.ProdutoRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.Produto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

/**
 * Interface mapper de {@link Produto}, {@link ProdutoDto} e {@link ProdutoDetalhesDto}.
 *
 * @author André Garcia
 * @see SubcategoriaMapper
 */
@Mapper(componentModel = ComponentModel.SPRING, uses = SubcategoriaMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProdutoMapper {

    Produto toProduto(ProdutoRegistroDto dto);

    Produto toProduto(ProdutoAtualizacaoDto dto);

    ProdutoDto toProdutoDto(Produto produto);

    ProdutoDetalhesDto toProdutoDetalhesDto(Produto produto);

}
