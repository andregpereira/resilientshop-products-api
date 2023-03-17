package com.github.andregpereira.resilientshop.productsapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.andregpereira.resilientshop.productsapi.dtos.ProdutoDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.ProdutoRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Produto;
import com.github.andregpereira.resilientshop.productsapi.mappers.ProdutoMapper;
import com.github.andregpereira.resilientshop.productsapi.repositories.ProdutoRepository;

import jakarta.transaction.Transactional;

@Service
public class ProdutoManutencaoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private ProdutoMapper produtoMapper;

	@Transactional
	public ProdutoDto registrar(ProdutoRegistroDto produtoRegistroDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public ProdutoDto atualizar(Long id, ProdutoRegistroDto produtoRegistroDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public String deletar(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Produto salvar(ProdutoRegistroDto produtoRegistroDto, Produto produtoAntigo) {
		// TODO Auto-generated method stub
		return null;
	}

}
