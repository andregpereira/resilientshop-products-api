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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoAtualizacaoDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.services.produto.ProdutoConsultaService;
import com.github.andregpereira.resilientshop.productsapi.services.produto.ProdutoManutencaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	@Autowired
	private ProdutoManutencaoService manutencaoService;

	@Autowired
	private ProdutoConsultaService consultaService;

	// Registrar produto
	@PostMapping
	public ResponseEntity<ProdutoDetalhesDto> registrar(@RequestBody @Valid ProdutoRegistroDto dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(manutencaoService.registrar(dto));
	}

	// Atualizar produto por id
	@PutMapping("/{id}")
	public ResponseEntity<ProdutoDetalhesDto> atualizar(@PathVariable Long id,
			@RequestBody @Valid ProdutoAtualizacaoDto dto) {
		return ResponseEntity.ok(manutencaoService.atualizar(id, dto));
	}

	// Remover por id
	@DeleteMapping("/{id}")
	private ResponseEntity<String> remover(@PathVariable Long id) {
		return ResponseEntity.ok(manutencaoService.remover(id));
	}

	// Listar todos os produtos
	@GetMapping
	public ResponseEntity<Page<ProdutoDto>> listar(
			@PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
		return ResponseEntity.ok(consultaService.listar(pageable));
	}

	// Pesquisar por id
	@GetMapping("/{id}")
	public ResponseEntity<ProdutoDetalhesDto> consultarPorId(@PathVariable Long id) {
		return ResponseEntity.ok(consultaService.consultarPorId(id));
	}

	// Pesquisar por nome
	@GetMapping("/nome")
	public ResponseEntity<Page<ProdutoDto>> consultarPorNome(@RequestParam String nome,
			@PageableDefault(sort = "nome", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
		return ResponseEntity.ok(consultaService.consultarPorNome(nome, pageable));
	}

	// Pesquisar por subcategoria
	@GetMapping("/subcategoria/{id}")
	public ResponseEntity<Page<ProdutoDto>> consultarPorSubcategoria(@PathVariable Long id,
			@PageableDefault(sort = "nome", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
		return ResponseEntity.ok(consultaService.consultarPorSubcategoria(id, pageable));
	}

	// Pesquisar por categoria
	@GetMapping("/categoria/{id}")
	public ResponseEntity<Page<ProdutoDto>> consultarPorCategoria(@PathVariable Long id,
			@PageableDefault(sort = "nome", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
		return ResponseEntity.ok(consultaService.consultarPorCategoria(id, pageable));
	}

}
