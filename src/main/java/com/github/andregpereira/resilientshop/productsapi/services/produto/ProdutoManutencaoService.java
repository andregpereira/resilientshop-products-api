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

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class ProdutoManutencaoService {

	@Autowired
	private ProdutoRepository repository;

	@Autowired
	private ProdutoMapper mapper;

	public ProdutoDetalhesDto registrar(ProdutoRegistroDto dto) {
		if (repository.existsBySku(dto.sku())) {
			throw new EntityExistsException("Já existe um produto com esse SKU registrado");
		}
		Produto produto = mapper.toProduto(dto);
		produto.setDataCriacao(LocalDateTime.now());
		return mapper.toProdutoDetalhesDto(repository.save(produto));
	}

	public ProdutoDetalhesDto atualizar(Long id, ProdutoAtualizacaoDto dto) {
		Optional<Produto> optionalProduto = repository.findById(id);
		if (!optionalProduto.isPresent()) {
			throw new EntityNotFoundException(
					"Desculpe, não foi possível encontrar um produto com este id. Verifique e tente novamente");
		}
		Produto produtoAntigo = optionalProduto.get();
		Produto produtoAtualizado = mapper.toProduto(dto);
		produtoAtualizado.setId(id);
		produtoAtualizado.setSku(produtoAntigo.getSku());
		produtoAtualizado.setDataCriacao(produtoAntigo.getDataCriacao());
		return mapper.toProdutoDetalhesDto(repository.save(produtoAtualizado));
	}

	public String remover(Long id) {
		Optional<Produto> optionalProduto = repository.findById(id);
		optionalProduto.ifPresentOrElse(p -> repository.deleteById(id), () -> {
			throw new EntityNotFoundException(
					"Desculpe, não foi possível encontrar um produto com este id. Verifique e tente novamente");
		});
		return "Produto removido.";
	}

}
