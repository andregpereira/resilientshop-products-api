package com.github.andregpereira.resilientshop.productsapi.services;

import java.security.InvalidParameterException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Produto;
import com.github.andregpereira.resilientshop.productsapi.mappers.ProdutoMapper;
import com.github.andregpereira.resilientshop.productsapi.repositories.ProdutoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProdutoConsultaService {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private ProdutoMapper produtoMapper;

	public ProdutoDto consultarPorId(Long id) {
		Optional<Produto> optionalProduto = produtoRepository.findById(id);
		if (!optionalProduto.isPresent()) {
			throw new EntityNotFoundException("produto_nao_encontrado");
		}
		return produtoMapper.toProdutoDto(optionalProduto.get());
	}

	public Page<ProdutoDto> consultarPorNome(String nome, Pageable pageable) {
		if (nome.isBlank()) {
			throw new InvalidParameterException("produto_consulta_nome_em_branco");
		} else if (nome.length() < 2) {
			throw new InvalidParameterException("produto_consulta_nome_tamanho_invalido");
		}
		Page<Produto> pageProdutos = produtoRepository.findByNome(nome, pageable);
		if (pageProdutos.isEmpty()) {
			throw new EmptyResultDataAccessException(1);
		}
		return ProdutoDto.criarLista(pageProdutos);
	}

}
