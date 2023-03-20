package com.github.andregpereira.resilientshop.productsapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoDetalhesDto;
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

	public ProdutoDetalhesDto consultarPorId(Long id) {
		if (!produtoRepository.existsById(id)) {
			throw new EntityNotFoundException("produto_nao_encontrado");
		}
		return produtoMapper.toProdutoDetalhesDto(produtoRepository.getReferenceById(id));
	}

	public Page<ProdutoDto> consultarPorNome(String nome, Pageable pageable) {
		nome = nome != null ? nome : "";
		Page<Produto> produtos = produtoRepository.findByNome(nome, pageable);
		if (produtos.isEmpty()) {
			throw new EmptyResultDataAccessException(1);
//			throw new InvalidParameterException("produto_consulta_nome_em_branco");
		}
		return ProdutoDto.criarLista(produtos);
//		else if (nome.length() < 2) {
//			throw new InvalidParameterException("produto_consulta_nome_tamanho_invalido");
//		}
//		if (produtos.getTotalElements()==0) {
//			throw new EmptyResultDataAccessException(1);
//		}
//		return ProdutoDto.criarLista(produtos);
	}

}
