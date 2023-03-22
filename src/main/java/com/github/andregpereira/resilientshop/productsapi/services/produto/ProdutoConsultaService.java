package com.github.andregpereira.resilientshop.productsapi.services.produto;

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
	private ProdutoRepository repository;

	@Autowired
	private ProdutoMapper mapper;

	public Page<ProdutoDto> listar(Pageable pageable) {
		return ProdutoDto.criarPage(repository.findAll(pageable));
	}

	public ProdutoDetalhesDto consultarPorId(Long id) {
		if (!repository.existsById(id)) {
			throw new EntityNotFoundException(
					"Desculpe, não foi possível encontrar um produto com este id. Verifique e tente novamente");
		}
		return mapper.toProdutoDetalhesDto(repository.getReferenceById(id));
	}

	public Page<ProdutoDto> consultarPorNome(String nome, Pageable pageable) {
		nome = nome.trim();
		Page<Produto> produtos = repository.findByNome(nome, pageable);
		if (produtos.isEmpty() || nome.isBlank()) {
			throw new EmptyResultDataAccessException(
					"Desculpe, não foi possível encontrar um produto com este nome. Verifique e tente novamente", 1);
		}
		return ProdutoDto.criarPage(produtos);
	}

}
