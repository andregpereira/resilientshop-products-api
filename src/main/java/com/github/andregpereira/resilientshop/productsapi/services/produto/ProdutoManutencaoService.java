package com.github.andregpereira.resilientshop.productsapi.services.produto;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Produto;
import com.github.andregpereira.resilientshop.productsapi.mappers.ProdutoMapper;
import com.github.andregpereira.resilientshop.productsapi.repositories.CategoriaRepository;
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
	private ProdutoMapper produtoMapper;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private SubcategoriaRepository subcategoriaRepository;

	public ProdutoDetalhesDto registrar(ProdutoRegistroDto dto) {
		if (produtoRepository.existsBySku(dto.sku())) {
			throw new EntityExistsException("Já existe um produto com este SKU");
		}
		Produto produto = produtoMapper.toProduto(dto);
		produto.setDataCriacao(LocalDateTime.now());
		return produtoMapper.toProdutoDetalhesDto(produtoRepository.save(produto));
	}

	public ProdutoDetalhesDto atualizar(Long id, ProdutoRegistroDto produtoRegistroDto) {
		// TODO Auto-generated method stub
		return null;
	}

	public String remover(Long id) {
		Optional<Produto> optionalProduto = produtoRepository.findById(id);
		optionalProduto.ifPresentOrElse(p -> produtoRepository.deleteById(id), () -> {
			throw new EntityNotFoundException(
					"Não foi possível encontrar um produto com este id. Verifique e tente novamente");
		});
		return "Produto removido.";
	}

}
