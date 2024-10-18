package com.github.andregpereira.resilientshop.productsapi.cross.mappers;

import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoAtualizacaoDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.ProdutoEntity;
import org.mapstruct.Mapper;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Interface mapper de {@link ProdutoEntity}, {@link ProdutoDto} e {@link ProdutoDetalhesDto}.
 *
 * @author André Garcia
 * @see SubcategoriaMapper
 */
@Mapper(
    componentModel = SPRING,
    uses = {CategoriaMapper.class, SubcategoriaMapper.class},
    injectionStrategy = CONSTRUCTOR,
    unmappedTargetPolicy = IGNORE
)
public interface ProdutoMapper {

    ProdutoEntity toProduto(ProdutoRegistroDto dto);

    ProdutoEntity toProduto(ProdutoAtualizacaoDto dto);

    ProdutoDto toProdutoDto(ProdutoEntity produto);

    ProdutoDetalhesDto toProdutoDetalhesDto(ProdutoEntity produto);

}
