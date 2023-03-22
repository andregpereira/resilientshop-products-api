package com.github.andregpereira.resilientshop.productsapi.services.categoria;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Categoria;
import com.github.andregpereira.resilientshop.productsapi.mappers.CategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.repositories.CategoriaRepository;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class CategoriaManutencaoService {

	@Autowired
	private CategoriaRepository repository;

	@Autowired
	private CategoriaMapper mapper;

	public CategoriaDto registrar(CategoriaRegistroDto dto) {
		if (repository.existsByNome(dto.nome())) {
			throw new EntityExistsException("Opa! Já existe uma categoria com esse nome");
		}
		Categoria categoria = mapper.toCategoria(dto);
		return mapper.toCategoriaDto(repository.save(categoria));
	}

	public CategoriaDto atualizar(Long id, CategoriaRegistroDto dto) {
		if (!repository.existsById(id)) {
			throw new EntityNotFoundException(
					"Desculpe, não foi possível encontrar uma categoria com este id. Verifique e tente novamente");
		} else if (!repository.existsByNome(dto.nome())) {
			throw new EntityExistsException("Opa! Já existe uma categoria com esse nome");
		}
		Categoria categoria = mapper.toCategoria(dto);
		categoria.setId(id);
		return mapper.toCategoriaDto(repository.save(categoria));
	}

	public String remover(Long id) {
		Optional<Categoria> optionalCategoria = repository.findById(id);
		optionalCategoria.ifPresentOrElse(c -> repository.deleteById(c.getId()), () -> {
			throw new EntityNotFoundException(
					"Desculpe, não foi possível encontrar uma categoria com este id. Verifique e tente novamente");
		});
		return "Categoria removida";
	}

}
