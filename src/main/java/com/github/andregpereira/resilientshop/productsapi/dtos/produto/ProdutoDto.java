package com.github.andregpereira.resilientshop.productsapi.dtos.produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.github.andregpereira.resilientshop.productsapi.entities.Produto;

import lombok.Builder;

@Builder
public record ProdutoDto(Long id, String nome, String descricao,
		@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy - HH:mm:ss") LocalDateTime dataCriacao,
		BigDecimal valorUnitario) {

	public ProdutoDto(Produto produto) {
		this(produto.getId(), produto.getNome(), produto.getDescricao(), produto.getDataCriacao(),
				produto.getValorUnitario());
	}

	public static Page<ProdutoDto> criarPage(Page<Produto> produtos) {
		return produtos.map(ProdutoDto::new);
	}

}
