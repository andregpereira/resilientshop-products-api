package com.github.andregpereira.resilientshop.productsapi.app.controllers;

import com.github.andregpereira.resilientshop.productsapi.app.dto.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.categoria.CategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.app.services.categoria.CategoriaConsultaService;
import com.github.andregpereira.resilientshop.productsapi.app.services.categoria.CategoriaManutencaoService;
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
 * Controller de categorias da API de Produtos.
 *
 * @author André Garcia
 */
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Categorias", description = "Operações de criação, atualização, remoção e consulta de categorias.")
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    /**
     * Injeção da dependência {@link CategoriaManutencaoService} para serviços de manutenção.
     */
    private final CategoriaManutencaoService manutencaoService;

    /**
     * Injeção da dependência {@link CategoriaConsultaService} para serviços de consulta.
     */
    private final CategoriaConsultaService consultaService;

    /**
     * Cadastra uma {@linkplain CategoriaRegistroDto categoria}.
     * Retorna uma {@linkplain CategoriaDto categoria}.
     *
     * @param dto a categoria a ser cadastrada.
     *
     * @return a categoria criada.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<CategoriaDto> criarCategoria(@Valid @RequestBody CategoriaRegistroDto dto) {
        log.info("Criando categoria...");
        CategoriaDto categoria = manutencaoService.criar(dto);
        URI uri = UriComponentsBuilder
            .fromPath("/categorias/{id}")
            .buildAndExpand(categoria.id())
            .toUri();
        log.info("Categoria criada com sucesso");
        return ResponseEntity
            .created(uri)
            .body(categoria);
    }

    /**
     * Atualiza uma {@linkplain CategoriaRegistroDto categoria} por {@code id}.
     * Retorna uma {@linkplain CategoriaDto categoria}.
     *
     * @param id  o id da categoria a ser atualizada.
     * @param dto a categoria a ser atualizada.
     *
     * @return a categoria atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDto> atualizarCategoria(
        @PathVariable Long id, @Valid @RequestBody CategoriaRegistroDto dto
    ) {
        log.info("Atualizando categoria com id {}...", id);
        return ResponseEntity.ok(manutencaoService.atualizar(id, dto));
    }

    /**
     * Remove uma categoria por {@code id}. Retorna uma mensagem de confirmação de remoção.
     *
     * @param id o id da categoria a ser removida.
     *
     * @return uma mensagem de confirmação de remoção.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removerCategoria(@PathVariable Long id) {
        log.info("Removendo categoria com id {}...", id);
        return ResponseEntity.ok(manutencaoService.remover(id));
    }

    /**
     * Lista todas as subcategorias cadastradas.
     * Retorna uma {@linkplain Page sublista} de {@linkplain CategoriaDto categorias}.
     *
     * @param pageable o pageable padrão.
     *
     * @return uma sublista de uma lista com todas as subcategorias cadastradas.
     */
    @GetMapping
    public ResponseEntity<Page<CategoriaDto>> listarCategorias(
        @PageableDefault(sort = "id") Pageable pageable
    ) {
        log.info("Listando categorias...");
        return ResponseEntity.ok(consultaService.listar(pageable));
    }

    /**
     * Pesquisa uma categoria pelo {@code id}.
     * Retorna uma {@linkplain CategoriaDto categoria}.
     *
     * @param id o id da categoria a ser consultada.
     *
     * @return uma categoria encontrada pelo {@code id}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDto> consultarCategoriaPorId(@PathVariable Long id) {
        log.info("Procurando categoria com id {}...", id);
        return ResponseEntity.ok(consultaService.consultarPorId(id));
    }

}
