package com.github.andregpereira.resilientshop.productsapi.app.controllers;

import com.github.andregpereira.resilientshop.productsapi.app.dtos.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.categoria.CategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.app.services.categoria.CategoriaConsultaService;
import com.github.andregpereira.resilientshop.productsapi.app.services.categoria.CategoriaManutencaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaManutencaoService manutencaoService;
    private final CategoriaConsultaService consultaService;

    // Registrar categoria
    @PostMapping
    public ResponseEntity<CategoriaDto> registrar(@RequestBody @Valid CategoriaRegistroDto dto,
            UriComponentsBuilder uriBuilder) {
        log.info("Criando categoria...");
        CategoriaDto categoria = manutencaoService.registrar(dto);
        URI uri = uriBuilder.path("/categorias/{id}").buildAndExpand(categoria.id()).toUri();
        log.info("Categoria criada com sucesso");
        return ResponseEntity.created(uri).body(categoria);
    }

    // Atualizar categoria por id
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDto> atualizar(@PathVariable Long id, @RequestBody @Valid CategoriaRegistroDto dto) {
        log.info("Atualizando categoria com id {}...", id);
        return ResponseEntity.ok(manutencaoService.atualizar(id, dto));
    }

    // Remover por id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> remover(@PathVariable Long id) {
        log.info("Removendo categoria com id {}...", id);
        return ResponseEntity.ok(manutencaoService.remover(id));
    }

    // Listar todas as categorias
    @GetMapping
    public ResponseEntity<Page<CategoriaDto>> listar(
            @PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
        log.info("Listando categorias...");
        return ResponseEntity.ok(consultaService.listar(pageable));
    }

    // Pesquisar por id
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDto> consultarPorId(@PathVariable Long id) {
        log.info("Procurando categoria com id {}...", id);
        return ResponseEntity.ok(consultaService.consultarPorId(id));
    }

}
