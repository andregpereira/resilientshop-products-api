package com.github.andregpereira.resilientshop.productsapi.app.controllers;

import com.github.andregpereira.resilientshop.productsapi.app.dtos.produto.*;
import com.github.andregpereira.resilientshop.productsapi.app.services.produto.ProdutoConsultaService;
import com.github.andregpereira.resilientshop.productsapi.app.services.produto.ProdutoManutencaoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@Slf4j
@Validated
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoManutencaoService manutencaoService;
    private final ProdutoConsultaService consultaService;

    // Registrar produto
    @PostMapping
    public ResponseEntity<ProdutoDetalhesDto> registrar(@RequestBody @Valid ProdutoRegistroDto dto,
            UriComponentsBuilder uriBuilder) {
        log.info("Criando produto...");
        ProdutoDetalhesDto produto = manutencaoService.registrar(dto);
        URI uri = uriBuilder.path("/produtos/{id}").buildAndExpand(produto.id()).toUri();
        log.info("Produto criado com sucesso");
        return ResponseEntity.created(uri).body(produto);
    }

    // Atualizar produto por id
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDetalhesDto> atualizar(@PathVariable Long id,
            @RequestBody @Valid ProdutoAtualizacaoDto dto) {
        log.info("Atualizando produto com id {}...", id);
        return ResponseEntity.ok(manutencaoService.atualizar(id, dto));
    }

    // Remover por id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> remover(@PathVariable Long id) {
        log.info("Removendo produto com id {}...", id);
        return ResponseEntity.ok(manutencaoService.remover(id));
    }

    @PutMapping("/subtrair/{id}")
    public void subtrair(@PathVariable Long id, @RequestBody ProdutoAtualizarEstoqueDto dto) {
        log.info("Subtraindo estoque do produto com id {}...", id);
        manutencaoService.subtrair(id, dto);
    }

    // Listar todos os produtos
    @GetMapping
    public ResponseEntity<Page<ProdutoDto>> listar(
            @PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
        log.info("Listando produtos...");
        return ResponseEntity.ok(consultaService.listar(pageable));
    }

    // Pesquisar por id
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDetalhesDto> consultarPorId(@PathVariable Long id) {
        log.info("Procurando produto com id {}...", id);
        return ResponseEntity.ok(consultaService.consultarPorId(id));
    }

    // Pesquisar por nome
    @GetMapping("/nome")
    public ResponseEntity<Page<ProdutoDto>> consultarPorNome(
            @RequestParam @Size(message = "O nome deve ter pelo menos 2 caracteres", min = 2) String nome,
            @PageableDefault(sort = "nome", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
        log.info("Procurando produto com nome {}...", nome.trim());
        return ResponseEntity.ok(consultaService.consultarPorNome(nome.trim(), pageable));
    }

    // Pesquisar por subcategoria
    @GetMapping("/subcategoria/{id}")
    public ResponseEntity<Page<ProdutoDto>> consultarPorSubcategoria(@PathVariable Long id,
            @PageableDefault(sort = "nome", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
        log.info("Procurando produto com subcategoria com id {}...", id);
        return ResponseEntity.ok(consultaService.consultarPorSubcategoria(id, pageable));
    }

    // Pesquisar por categoria
    @GetMapping("/categoria/{id}")
    public ResponseEntity<Page<ProdutoDto>> consultarPorCategoria(@PathVariable Long id,
            @PageableDefault(sort = "nome", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
        log.info("Procurando produto com categoria com id {}...", id);
        return ResponseEntity.ok(consultaService.consultarPorCategoria(id, pageable));
    }

}
