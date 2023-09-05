package com.github.andregpereira.resilientshop.productsapi.cross.mappers;

import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoAtualizacaoDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.ProdutoEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

/**
 * Interface mapper de {@link ProdutoEntity}, {@link ProdutoDto} e {@link ProdutoDetalhesDto}.
 *
 * @author André Garcia
 * @see SubcategoriaMapper
 */
@Mapper(componentModel = ComponentModel.SPRING, uses = SubcategoriaMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProdutoMapper {

    ProdutoEntity toProduto(ProdutoRegistroDto dto);

    ProdutoEntity toProduto(ProdutoAtualizacaoDto dto);

    ProdutoDto toProdutoDto(ProdutoEntity produto);

    ProdutoDetalhesDto toProdutoDetalhesDto(ProdutoEntity produto);

}
