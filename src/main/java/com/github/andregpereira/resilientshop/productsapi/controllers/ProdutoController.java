package com.github.andregpereira.resilientshop.productsapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.github.andregpereira.resilientshop.productsapi.dtos.ProdutoDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.ProdutoRegistroDto;
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

	// Registrar usuário
	@PostMapping
	public ResponseEntity<ProdutoDto> registrar(@RequestBody @Valid ProdutoRegistroDto usuarioRegistroDto) {
		ProdutoDto produtoDto = produtoManutencaoService.registrar(usuarioRegistroDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(produtoDto);
	}

	// Atualizar usuário por id
	@PutMapping("/{id}")
	public ResponseEntity<ProdutoDto> atualizar(@PathVariable Long id,
			@RequestBody @Valid ProdutoRegistroDto usuarioRegistroDto) {
		return ResponseEntity.ok(produtoManutencaoService.atualizar(id, usuarioRegistroDto));
	}

	// Deletar por id
	@DeleteMapping("/{id}")
	private ResponseEntity<String> deletar(@PathVariable Long id) {
		return ResponseEntity.ok(produtoManutencaoService.deletar(id));
	}

	// Pesquisar por id
	@GetMapping("/{id}")
	public ResponseEntity<ProdutoDto> consultarPorId(@PathVariable Long id) {
		return ResponseEntity.ok(produtoConsultaService.consultarPorId(id));
	}

}
