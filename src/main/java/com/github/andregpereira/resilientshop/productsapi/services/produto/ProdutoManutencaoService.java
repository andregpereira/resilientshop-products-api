package com.github.andregpereira.resilientshop.productsapi.services.produto;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoAtualizacaoDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Produto;
import com.github.andregpereira.resilientshop.productsapi.mappers.ProdutoMapper;
import com.github.andregpereira.resilientshop.productsapi.repositories.ProdutoRepository;
import com.github.andregpereira.resilientshop.productsapi.repositories.SubcategoriaRepository;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class ProdutoManutencaoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private ProdutoMapper mapper;

	@Autowired
	private SubcategoriaRepository subcategoriaRepository;

	public ProdutoDetalhesDto registrar(ProdutoRegistroDto dto) {
		if (produtoRepository.existsBySku(dto.sku())) {
			throw new EntityExistsException("Opa! Já existe um produto com esse SKU registrado");
		} else if (produtoRepository.existsByNome(dto.nome())) {
			throw new EntityExistsException("Opa! Já existe um produto com esse nome registrado");
		} else if (!subcategoriaRepository.existsById(dto.idSubcategoria())) {
			throw new EntityNotFoundException(
					"Desculpe, não foi possível encontrar uma subcategoria com este id. Verifique e tente novamente");
		}
		Produto produto = mapper.toProduto(dto);
		produto.setDataCriacao(LocalDateTime.now());
		produto.setSubcategoria(subcategoriaRepository.getReferenceById(dto.idSubcategoria()));
		return mapper.toProdutoDetalhesDto(produtoRepository.save(produto));
	}

	public ProdutoDetalhesDto atualizar(Long id, ProdutoAtualizacaoDto dto) {
		Optional<Produto> optionalProduto = produtoRepository.findById(id);
		if (!optionalProduto.isPresent()) {
			throw new EntityNotFoundException(
					"Desculpe, não foi possível encontrar um produto com este id. Verifique e tente novamente");
		} else if (produtoRepository.existsByNome(dto.nome())) {
			throw new EntityExistsException("Opa! Já existe um produto com esse nome registrado");
		} else if (!subcategoriaRepository.existsById(dto.idSubcategoria())) {
			throw new EntityNotFoundException(
					"Desculpe, não foi possível encontrar uma subcategoria com este id. Verifique e tente novamente");
		}
		Produto produtoAntigo = optionalProduto.get();
		Produto produtoAtualizado = mapper.toProduto(dto);
		produtoAtualizado.setId(id);
		produtoAtualizado.setSku(produtoAntigo.getSku());
		produtoAtualizado.setDataCriacao(produtoAntigo.getDataCriacao());
		return mapper.toProdutoDetalhesDto(produtoRepository.save(produtoAtualizado));
	}

	public String remover(Long id) {
		produtoRepository.findById(id).ifPresentOrElse(p -> produtoRepository.deleteById(id), () -> {
			throw new EntityNotFoundException(
					"Desculpe, não foi possível encontrar um produto com este id. Verifique e tente novamente");
		});
		return "Produto removido.";
	}

}
