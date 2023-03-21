package com.github.andregpereira.resilientshop.productsapi.services.categoria;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaDto;

@Service
public class CategoriaConsultaService {

	public CategoriaDetalhesDto consultarPorId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<CategoriaDto> consultarPorNome(String nome, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

}
