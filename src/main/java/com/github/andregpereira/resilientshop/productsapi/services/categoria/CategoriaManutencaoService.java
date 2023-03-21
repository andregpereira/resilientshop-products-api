package com.github.andregpereira.resilientshop.productsapi.services.categoria;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaRegistroDto;

@Service
@Transactional
public class CategoriaManutencaoService {

	public CategoriaDetalhesDto registrar(CategoriaRegistroDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	public CategoriaDetalhesDto atualizar(Long id, CategoriaRegistroDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	public String remover(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
