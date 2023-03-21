package com.github.andregpereira.resilientshop.productsapi.services.subcategoria;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaRegistroDto;

@Service
@Transactional
public class SubcategoriaManutencaoService {

	public SubcategoriaDto registrar(SubcategoriaRegistroDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	public SubcategoriaDto atualizar(Long id, SubcategoriaRegistroDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	public String remover(Long id) {
		// TODO Auto-generated method stub
		return "Subcategoria removida";
	}

}
