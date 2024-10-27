package com.github.andregpereira.resilientshop.productsapi.app.controllers;

import com.github.andregpereira.resilientshop.productsapi.app.dto.subcategoria.SubcategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.subcategoria.SubcategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.subcategoria.SubcategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.app.services.subcategoria.SubcategoriaConsultaService;
import com.github.andregpereira.resilientshop.productsapi.app.services.subcategoria.SubcategoriaManutencaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Controller de subcategorias da API de Produtos.
 *
 * @author André Garcia
 */
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Subcategorias", description = "Operações de criação, atualização, remoção e consulta de subcategorias.")
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
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<SubcategoriaDetalhesDto> criarSubcategoria(@RequestBody @Valid SubcategoriaRegistroDto dto) {
        log.info("Criando subcategoria...");
        SubcategoriaDetalhesDto subcategoria = manutencaoService.criar(dto);
        URI uri = UriComponentsBuilder
            .fromPath("/categorias/{id}")
            .buildAndExpand(subcategoria.id())
            .toUri();
        log.info("Subcategoria criada com sucesso");
        return ResponseEntity
            .created(uri)
            .body(subcategoria);
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
    public ResponseEntity<SubcategoriaDetalhesDto> atualizarSubcategoria(
        @PathVariable Long id, @Valid @RequestBody SubcategoriaRegistroDto dto
    ) {
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
    public ResponseEntity<String> removerSubcategoria(@PathVariable Long id) {
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
    public ResponseEntity<Page<SubcategoriaDto>> listarSubcategorias(
        @PageableDefault(sort = "id") Pageable pageable
    ) {
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
    public ResponseEntity<SubcategoriaDetalhesDto> consultarSubcategoriaPorId(@PathVariable Long id) {
        log.info("Procurando subcategoria com id {}...", id);
        return ResponseEntity.ok(consultaService.consultarPorId(id));
    }

}
