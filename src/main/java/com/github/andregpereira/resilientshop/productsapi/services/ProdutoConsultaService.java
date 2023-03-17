package com.github.andregpereira.resilientshop.productsapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.andregpereira.resilientshop.productsapi.dtos.ProdutoDto;
import com.github.andregpereira.resilientshop.productsapi.mappers.ProdutoMapper;
import com.github.andregpereira.resilientshop.productsapi.repositories.ProdutoRepository;

@Service
public class ProdutoConsultaService {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private ProdutoMapper produtoMapper;

	public ProdutoDto consultarPorId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
