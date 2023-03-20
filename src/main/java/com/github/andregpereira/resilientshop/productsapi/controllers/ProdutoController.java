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

import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.services.ProdutoConsultaService;
import com.github.andregpereira.resilientshop.productsapi.services.ProdutoManutencaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	@Autowired
	private ProdutoManutencaoService produtoManutencaoService;

	@Autowired
	private ProdutoConsultaService produtoConsultaService;

	// Registrar produto
	@PostMapping
	public ResponseEntity<ProdutoDto> registrar(@RequestBody @Valid ProdutoRegistroDto produtoRegistroDto) {
		ProdutoDto produtoDto = produtoManutencaoService.registrar(produtoRegistroDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(produtoDto);
	}

	// Atualizar produto por id
	@PutMapping("/{id}")
	public ResponseEntity<ProdutoDto> atualizar(@PathVariable Long id,
			@RequestBody @Valid ProdutoRegistroDto produtoRegistroDto) {
		return ResponseEntity.ok(produtoManutencaoService.atualizar(id, produtoRegistroDto));
	}

	// Remover por id
	@DeleteMapping("/{id}")
	private ResponseEntity<String> remover(@PathVariable Long id) {
		return ResponseEntity.ok(produtoManutencaoService.remover(id));
	}

	// Pesquisar por id
	@GetMapping("/{id}")
	public ResponseEntity<ProdutoDetalhesDto> consultarPorId(@PathVariable Long id) {
		return ResponseEntity.ok(produtoConsultaService.consultarPorId(id));
	}

	// Pesquisar por nome
	@GetMapping("/nome")
	public ResponseEntity<Page<ProdutoDto>> consultarPorNome(@RequestParam(required = true) String nome,
			@PageableDefault(sort = "nome", direction = Direction.DESC, page = 0, size = 10) Pageable pageable) {
		return ResponseEntity.ok(produtoConsultaService.consultarPorNome(nome, pageable));
	}

}
