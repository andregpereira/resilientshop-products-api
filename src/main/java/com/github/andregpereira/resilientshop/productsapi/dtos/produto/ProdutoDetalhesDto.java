package com.github.andregpereira.resilientshop.productsapi.dtos.produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Produto;

import lombok.Builder;

@Builder
public record ProdutoDetalhesDto(Long id, Long sku, String nome, String descricao,
		@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy - HH:mm") LocalDateTime dataCriacao,
		BigDecimal valorUnitario, int estoque, CategoriaDto categoria) {

	public ProdutoDetalhesDto(Produto produto) {
		this(produto.getId(), produto.getSku(), produto.getNome(), produto.getDescricao(), produto.getDataCriacao(),
				produto.getValorUnitario(), produto.getEstoque(), new CategoriaDto(produto.getCategoria()));
	}

	public static Page<ProdutoDetalhesDto> criarLista(Page<Produto> pageProdutos) {
		return pageProdutos.map(ProdutoDetalhesDto::new);
	}

}
