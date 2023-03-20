package com.github.andregpereira.resilientshop.productsapi.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Categoria;
import com.github.andregpereira.resilientshop.productsapi.entities.Produto;
import com.github.andregpereira.resilientshop.productsapi.entities.Subcategoria;
import com.github.andregpereira.resilientshop.productsapi.mappers.ProdutoMapper;
import com.github.andregpereira.resilientshop.productsapi.repositories.CategoriaRepository;
import com.github.andregpereira.resilientshop.productsapi.repositories.ProdutoRepository;
import com.github.andregpereira.resilientshop.productsapi.repositories.SubcategoriaRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ProdutoManutencaoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private SubcategoriaRepository subcategoriaRepository;

	@Autowired
	private ProdutoMapper produtoMapper;

	@Transactional
	public ProdutoDto registrar(ProdutoRegistroDto produtoRegistroDto) {
		if (produtoRepository.findBySku(produtoRegistroDto.sku()).isPresent()) {
			throw new DataIntegrityViolationException("produto_existente");
		}
		Produto produtoRegistrado = salvar(produtoRegistroDto, null);
		produtoRegistrado.setDataCriacao(LocalDateTime.now());
		return produtoMapper.toProdutoDto(produtoRepository.save(produtoRegistrado));
	}

	@Transactional
	public ProdutoDto atualizar(Long id, ProdutoRegistroDto produtoRegistroDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public String remover(Long id) {
		Optional<Produto> usuarioOptional = produtoRepository.findById(id);
		usuarioOptional.ifPresentOrElse(p -> produtoRepository.deleteById(id), () -> {
			throw new EntityNotFoundException("produto_nao_encontrado");
		});
		return "Produto removido.";
	}

	public Produto salvar(ProdutoRegistroDto produtoRegistroDto, Produto produtoAntigo) {
		Produto produto = produtoMapper.toProduto(produtoRegistroDto);
		Categoria categoria = produto.getCategoria();
		Subcategoria subcategoria = categoria.getSubcategoria();
		if (produtoAntigo != null) {
			produto.setDataCriacao(produtoAntigo.getDataCriacao());
		}
		categoriaRepository.save(categoria);
		subcategoriaRepository.save(subcategoria);
		return produto;
	}

}
