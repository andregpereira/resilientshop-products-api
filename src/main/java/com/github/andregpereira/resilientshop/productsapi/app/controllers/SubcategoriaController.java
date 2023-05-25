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

/**
 * Controller de subcategorias da API de Produtos.
 *
 * @author André Garcia
 */
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/subcategorias")
public class SubcategoriaController {

    /**
     * Injeção da dependência {@link SubcategoriaManutencaoService} para serviços de manutenção.
     */
    private final SubcategoriaManutencaoService manutencaoService;

    /**
     * Injeção da dependência {@link SubcategoriaConsultaService} para serviços de consulta.
     */
    private final SubcategoriaConsultaService consultaService;

    /**
     * Cadastra uma {@linkplain SubcategoriaRegistroDto subcategoria}.
     * Retorna uma {@linkplain SubcategoriaDetalhesDto subcategoria detalhada}.
     *
     * @param dto a subcategoria a ser cadastrada.
     *
     * @return a subcategoria criada.
     */
    @PostMapping
    public ResponseEntity<SubcategoriaDetalhesDto> registrar(@RequestBody @Valid SubcategoriaRegistroDto dto) {
        log.info("Criando subcategoria...");
        SubcategoriaDetalhesDto subcategoria = manutencaoService.criar(dto);
        URI uri = UriComponentsBuilder.fromPath("/categorias/{id}").buildAndExpand(subcategoria.id()).toUri();
        log.info("Subcategoria criada com sucesso");
        return ResponseEntity.created(uri).body(subcategoria);
    }

    /**
     * Atualiza uma {@linkplain SubcategoriaRegistroDto subcategoria} por {@code id}.
     * Retorna uma {@linkplain SubcategoriaDetalhesDto subcategoria detalhada}.
     *
     * @param id  o id da subcategoria a ser atualizada.
     * @param dto a subcategoria a ser atualizada.
     *
     * @return a subcategoria atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubcategoriaDetalhesDto> atualizar(@PathVariable Long id,
            @RequestBody @Valid SubcategoriaRegistroDto dto) {
        log.info("Atualizando subcategoria com id {}...", id);
        return ResponseEntity.ok(manutencaoService.atualizar(id, dto));
    }

    /**
     * Remove uma subcategoria por {@code id}. Retorna uma mensagem de confirmação de remoção.
     *
     * @param id o id da subcategoria a ser removida.
     *
     * @return uma mensagem de confirmação de remoção.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> remover(@PathVariable Long id) {
        log.info("Removendo subcategoria com id {}...", id);
        return ResponseEntity.ok(manutencaoService.remover(id));
    }

    /**
     * Lista todas as subcategorias cadastradas.
     * Retorna uma {@linkplain Page sublista} de {@linkplain SubcategoriaDto subcategorias}.
     *
     * @param pageable o pageable padrão.
     *
     * @return uma sublista de uma lista com todas as subcategorias cadastradas.
     */
    @GetMapping
    public ResponseEntity<Page<SubcategoriaDto>> listar(
            @PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
        log.info("Listando subcategorias...");
        return ResponseEntity.ok(consultaService.listar(pageable));
    }

    /**
     * Pesquisa uma subcategoria pelo {@code id}.
     * Retorna uma {@linkplain SubcategoriaDetalhesDto subcategoria detalhada}.
     *
     * @param id o id da subcategoria a ser consultada.
     *
     * @return uma subcategoria encontrada pelo {@code id}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubcategoriaDetalhesDto> consultarPorId(@PathVariable Long id) {
        log.info("Procurando subcategoria com id {}...", id);
        return ResponseEntity.ok(consultaService.consultarPorId(id));
    }

}
