package com.github.andregpereira.resilientshop.productsapi.services.subcategoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Subcategoria;
import com.github.andregpereira.resilientshop.productsapi.mappers.SubcategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.repositories.CategoriaRepository;
import com.github.andregpereira.resilientshop.productsapi.repositories.SubcategoriaRepository;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class SubcategoriaManutencaoService {

	@Autowired
	private SubcategoriaRepository subcategoriaRepository;

	@Autowired
	private SubcategoriaMapper mapper;

	@Autowired
	private CategoriaRepository categoriaRepository;

	public SubcategoriaDetalhesDto registrar(SubcategoriaRegistroDto dto) {
		if (!categoriaRepository.existsById(dto.idCategoria())) {
			throw new EntityNotFoundException(
					"Desculpe, não foi possível encontrar uma categoria com este id. Verifique e tente novamente");
		} else if (subcategoriaRepository.existsByNome(dto.nome())) {
			throw new EntityExistsException("Opa! Já existe uma subcategoria com esse nome");
		}
		Subcategoria subcategoria = mapper.toSubcategoria(dto);
		subcategoria.setCategoria(categoriaRepository.getReferenceById(dto.idCategoria()));
		return mapper.toSubcategoriaDetalhesDto(subcategoriaRepository.save(subcategoria));
	}

	public SubcategoriaDetalhesDto atualizar(Long id, SubcategoriaRegistroDto dto) {
		if (!subcategoriaRepository.existsById(id)) {
			throw new EntityNotFoundException(
					"Desculpe, não foi possível encontrar uma subcategoria com este id. Verifique e tente novamente");
		} else if (!categoriaRepository.existsById(dto.idCategoria())) {
			throw new EntityNotFoundException(
					"Desculpe, não foi possível encontrar uma categoria com este id. Verifique e tente novamente");
		} else if (subcategoriaRepository.existsByNome(dto.nome())) {
			throw new EntityExistsException("Opa! Já existe uma subcategoria com esse nome");
		}
		Subcategoria subcategoriaAtualizada = mapper.toSubcategoria(dto);
		subcategoriaAtualizada.setId(id);
		subcategoriaAtualizada.setCategoria(categoriaRepository.getReferenceById(dto.idCategoria()));
		return mapper.toSubcategoriaDetalhesDto(subcategoriaRepository.save(subcategoriaAtualizada));
	}

	public String remover(Long id) {
		subcategoriaRepository.findById(id).ifPresentOrElse(c -> subcategoriaRepository.deleteById(c.getId()), () -> {
			throw new EntityNotFoundException(
					"Desculpe, não foi possível encontrar uma subcategoria com este id. Verifique e tente novamente");
		});
		return "Subcategoria removida";
	}

}
