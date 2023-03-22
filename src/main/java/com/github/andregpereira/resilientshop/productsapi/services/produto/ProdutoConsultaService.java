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
import com.github.andregpereira.resilientshop.productsapi.repositories.CategoriaRepository;
import com.github.andregpereira.resilientshop.productsapi.repositories.ProdutoRepository;
import com.github.andregpereira.resilientshop.productsapi.repositories.SubcategoriaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProdutoConsultaService {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private ProdutoMapper mapper;

	@Autowired
	private SubcategoriaRepository subcategoriaRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	public Page<ProdutoDto> listar(Pageable pageable) {
		return ProdutoDto.criarPage(produtoRepository.findAll(pageable));
	}

	public ProdutoDetalhesDto consultarPorId(Long id) {
		if (!produtoRepository.existsById(id)) {
			throw new EntityNotFoundException(
					"Desculpe, não foi possível encontrar um produto com este id. Verifique e tente novamente");
		}
		return mapper.toProdutoDetalhesDto(produtoRepository.getReferenceById(id));
	}

	public Page<ProdutoDto> consultarPorNome(String nome, Pageable pageable) {
		nome = nome.trim();
		Page<Produto> produtos = produtoRepository.findByNome(nome, pageable);
		if (produtos.isEmpty() || nome.isBlank()) {
			throw new EmptyResultDataAccessException(
					"Desculpe, não foi possível encontrar um produto com este nome. Verifique e tente novamente", 1);
		}
		return ProdutoDto.criarPage(produtos);
	}

	public Page<ProdutoDto> consultarPorSubcategoria(Long id, Pageable pageable) {
		if (!subcategoriaRepository.existsById(id)) {
			throw new EntityNotFoundException(
					"Desculpe, não foi possível encontrar um produto com este id. Verifique e tente novamente");
		}
		Page<Produto> produtos = produtoRepository.findAllBySubcategoriaId(id, pageable);
		if (produtos.isEmpty()) {
			throw new EmptyResultDataAccessException("Ops! Nenhum produto foi encontrado com essa subcategoria", 1);
		}
		return ProdutoDto.criarPage(produtos);
	}

	public Page<ProdutoDto> consultarPorCategoria(Long id, Pageable pageable) {
		if (!categoriaRepository.existsById(id)) {
			throw new EntityNotFoundException(
					"Desculpe, não foi possível encontrar um produto com este id. Verifique e tente novamente");
		}
		Page<Produto> produtos = produtoRepository.findAllBySubcategoriaCategoriaId(id, pageable);
		if (produtos.isEmpty()) {
			throw new EmptyResultDataAccessException("Ops! Nenhum produto foi encontrado com essa categoria", 1);
		}
		return ProdutoDto.criarPage(produtos);
	}

}
