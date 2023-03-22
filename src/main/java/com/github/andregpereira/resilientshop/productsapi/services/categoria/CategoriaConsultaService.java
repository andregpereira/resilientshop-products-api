package com.github.andregpereira.resilientshop.productsapi.services.categoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.mappers.CategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.repositories.CategoriaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoriaConsultaService {

	@Autowired
	private CategoriaRepository repository;

	@Autowired
	private CategoriaMapper mapper;

	public Page<CategoriaDto> listar(Pageable pageable) {
		return CategoriaDto.criarPage(repository.findAll(pageable));
	}

	public CategoriaDto consultarPorId(Long id) {
		if (!repository.existsById(id)) {
			throw new EntityNotFoundException(
					"Desculpe, não foi possível encontrar uma categoria com este id. Verifique e tente novamente");
		}
		return mapper.toCategoriaDto(repository.getReferenceById(id));
	}

}
