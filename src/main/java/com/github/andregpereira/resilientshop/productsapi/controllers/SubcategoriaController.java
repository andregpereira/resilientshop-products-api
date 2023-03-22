package com.github.andregpereira.resilientshop.productsapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.services.subcategoria.SubcategoriaConsultaService;
import com.github.andregpereira.resilientshop.productsapi.services.subcategoria.SubcategoriaManutencaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/subcategorias")
public class SubcategoriaController {

	@Autowired
	private SubcategoriaManutencaoService manutencaoService;

	@Autowired
	private SubcategoriaConsultaService consultaService;

	// Registrar subcategoria
	@PostMapping
	public ResponseEntity<SubcategoriaDto> registrar(@RequestBody @Valid SubcategoriaRegistroDto dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(manutencaoService.registrar(dto));
	}

	// Atualizar subcategoria por id
	@PutMapping("/{id}")
	public ResponseEntity<SubcategoriaDto> atualizar(@PathVariable Long id,
			@RequestBody @Valid SubcategoriaRegistroDto dto) {
		return ResponseEntity.ok(manutencaoService.atualizar(id, dto));
	}

	// Remover por id
	@DeleteMapping("/{id}")
	private ResponseEntity<String> remover(@PathVariable Long id) {
		return ResponseEntity.ok(manutencaoService.remover(id));
	}

	// Listar todas as subcategorias
	@GetMapping
	public ResponseEntity<Page<SubcategoriaDto>> listar(
			@PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
		return ResponseEntity.ok(consultaService.listar(pageable));
	}

	// Pesquisar por id
	@GetMapping("/{id}")
	public ResponseEntity<SubcategoriaDto> consultarPorId(@PathVariable Long id) {
		return ResponseEntity.ok(consultaService.consultarPorId(id));
	}

}
