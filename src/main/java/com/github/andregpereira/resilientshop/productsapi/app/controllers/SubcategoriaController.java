package com.github.andregpereira.resilientshop.productsapi.app.controllers;

import com.github.andregpereira.resilientshop.productsapi.app.dtos.subcategoria.SubcategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.subcategoria.SubcategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.subcategoria.SubcategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.app.services.subcategoria.SubcategoriaConsultaService;
import com.github.andregpereira.resilientshop.productsapi.app.services.subcategoria.SubcategoriaManutencaoService;
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
@RequestMapping("/subcategorias")
public class SubcategoriaController {

    private final SubcategoriaManutencaoService manutencaoService;
    private final SubcategoriaConsultaService consultaService;

    // Registrar subcategoria
    @PostMapping
    public ResponseEntity<SubcategoriaDetalhesDto> registrar(@RequestBody @Valid SubcategoriaRegistroDto dto,
            UriComponentsBuilder uriBuilder) {
        log.info("Criando subcategoria...");
        SubcategoriaDetalhesDto subcategoria = manutencaoService.registrar(dto);
        URI uri = uriBuilder.path("/categorias/{id}").buildAndExpand(subcategoria.id()).toUri();
        log.info("Subcategoria criada com sucesso");
        return ResponseEntity.created(uri).body(subcategoria);
    }

    // Atualizar subcategoria por id
    @PutMapping("/{id}")
    public ResponseEntity<SubcategoriaDetalhesDto> atualizar(@PathVariable Long id,
            @RequestBody @Valid SubcategoriaRegistroDto dto) {
        log.info("Atualizando subcategoria com id {}...", id);
        return ResponseEntity.ok(manutencaoService.atualizar(id, dto));
    }

    // Remover por id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> remover(@PathVariable Long id) {
        log.info("Removendo subcategoria com id {}...", id);
        return ResponseEntity.ok(manutencaoService.remover(id));
    }

    // Listar todas as subcategorias
    @GetMapping
    public ResponseEntity<Page<SubcategoriaDto>> listar(
            @PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
        log.info("Listando subcategorias...");
        return ResponseEntity.ok(consultaService.listar(pageable));
    }

    // Pesquisar por id
    @GetMapping("/{id}")
    public ResponseEntity<SubcategoriaDetalhesDto> consultarPorId(@PathVariable Long id) {
        log.info("Procurando subcategoria com id {}...", id);
        return ResponseEntity.ok(consultaService.consultarPorId(id));
    }

}
