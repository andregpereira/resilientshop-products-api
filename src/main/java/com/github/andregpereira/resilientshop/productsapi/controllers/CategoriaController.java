package com.github.andregpereira.resilientshop.productsapi.controllers;

import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.services.categoria.CategoriaConsultaService;
import com.github.andregpereira.resilientshop.productsapi.services.categoria.CategoriaManutencaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaManutencaoService manutencaoService;

    @Autowired
    private CategoriaConsultaService consultaService;

    // Registrar categoria
    @PostMapping
    public ResponseEntity<CategoriaDto> registrar(@RequestBody @Valid CategoriaRegistroDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(manutencaoService.registrar(dto));
    }

    // Atualizar categoria por id
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDto> atualizar(@PathVariable Long id, @RequestBody @Valid CategoriaRegistroDto dto) {
        return ResponseEntity.ok(manutencaoService.atualizar(id, dto));
    }

    // Remover por id
    @DeleteMapping("/{id}")
    private ResponseEntity<String> remover(@PathVariable Long id) {
        return ResponseEntity.ok(manutencaoService.remover(id));
    }

    // Listar todas as categorias
    @GetMapping
    public ResponseEntity<Page<CategoriaDto>> listar(
            @PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(consultaService.listar(pageable));
    }

    // Pesquisar por id
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDto> consultarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.consultarPorId(id));
    }

}
