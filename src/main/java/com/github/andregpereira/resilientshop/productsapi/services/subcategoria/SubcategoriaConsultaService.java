package com.github.andregpereira.resilientshop.productsapi.services.subcategoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.mappers.SubcategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.repositories.SubcategoriaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SubcategoriaConsultaService {

	@Autowired
	private SubcategoriaRepository repository;

	@Autowired
	private SubcategoriaMapper mapper;

	public Page<SubcategoriaDto> listar(Pageable pageable) {
		return SubcategoriaDto.criarPage(repository.findAll(pageable));
	}

	public SubcategoriaDetalhesDto consultarPorId(Long id) {
		if (!repository.existsById(id)) {
			throw new EntityNotFoundException(
					"Desculpe, não foi possível encontrar uma subcategoria com este id. Verifique e tente novamente");
		}
		return mapper.toSubcategoriaDetalhesDto(repository.getReferenceById(id));
	}

}
