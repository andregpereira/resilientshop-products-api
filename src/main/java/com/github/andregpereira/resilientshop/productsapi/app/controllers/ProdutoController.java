package com.github.andregpereira.resilientshop.productsapi.app.controllers;

import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.*;
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
import java.util.List;
import java.util.Set;

/**
 * Controller de produtos da API de Produtos.
 *
 * @author André Garcia
 */
@RequiredArgsConstructor
@Slf4j
@Validated
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    /**
     * Injeção da dependência {@link ProdutoManutencaoService} para serviços de manutenção.
     */
    private final ProdutoManutencaoService manutencaoService;

    /**
     * Injeção da dependência {@link ProdutoConsultaService} para serviços de consulta.
     */
    private final ProdutoConsultaService consultaService;

    /**
     * Cadastra um {@linkplain ProdutoRegistroDto produto}.
     * Retorna um {@linkplain ProdutoDetalhesDto produto detalhado}.
     *
     * @param dto o produto a ser cadastrado.
     *
     * @return o produto criado.
     */
    @PostMapping
    public ResponseEntity<ProdutoDetalhesDto> criar(@RequestBody @Valid ProdutoRegistroDto dto) {
        log.info("Criando produto...");
        ProdutoDetalhesDto produto = manutencaoService.criar(dto);
        log.info("Produto criado com sucesso");
        URI uri = UriComponentsBuilder.fromPath("/produtos/{id}").buildAndExpand(produto.id()).toUri();
        return ResponseEntity.created(uri).body(produto);
    }

    /**
     * Atualiza um {@linkplain ProdutoAtualizacaoDto produto} por {@code id}.
     * Retorna um {@linkplain ProdutoDetalhesDto produto detalhado}.
     *
     * @param id  o id do produto a ser atualizado.
     * @param dto os dados do produto a serem atualizados.
     *
     * @return o produto atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDetalhesDto> atualizar(@PathVariable Long id,
            @RequestBody @Valid ProdutoAtualizacaoDto dto) {
        log.info("Atualizando produto com id {}...", id);
        ProdutoDetalhesDto produto = manutencaoService.atualizar(id, dto);
        log.info("Produto com id {} atualizado", id);
        URI uri = UriComponentsBuilder.fromPath("/produtos/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.ok().location(uri).body(produto);
    }

    /**
     * Remove um produto por {@code id}.
     * Retorna uma mensagem de confirmação de remoção.
     *
     * @param id o id do produto a ser removido.
     *
     * @return uma mensagem de confirmação de remoção.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> remover(@PathVariable Long id) {
        log.info("Removendo produto com id {}...", id);
        return ResponseEntity.ok(manutencaoService.remover(id));
    }

    /**
     * Subtrai produtos do estoque, dado a lista de {@linkplain ProdutoAtualizarEstoqueDto produtos}.
     *
     * @param dtos a lista de produtos a terem seus estoques subtraídos.
     */
    @PutMapping("/estoque/subtrair")
    public ResponseEntity<Void> subtrairEstoque(@RequestBody Set<ProdutoAtualizarEstoqueDto> dtos) {
        log.info("Subtraindo produtos do estoque...");
        manutencaoService.subtrairEstoque(dtos);
        return ResponseEntity.ok().build();
    }

    /**
     * Retorna produtos ao estoque, dado a lista de {@linkplain ProdutoAtualizarEstoqueDto produtos}.
     *
     * @param dtos a lista de produtos a serem retornados ao estoque.
     */
    @PutMapping("/estoque/retornar")
    public ResponseEntity<Void> retornarEstoque(@RequestBody Set<ProdutoAtualizarEstoqueDto> dtos) {
        log.info("Retornando produtos ao estoque...");
        manutencaoService.retornarEstoque(dtos);
        return ResponseEntity.ok().build();
    }

    /**
     * Lista todos os produtos cadastrados.
     * Retorna uma {@linkplain Page sublista} de {@linkplain ProdutoDto produtos}.
     *
     * @param pageable o pageable padrão.
     *
     * @return uma sublista de uma lista com todos os produtos cadastrados.
     */
    @GetMapping
    public ResponseEntity<Page<ProdutoDto>> listar(
            @PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
        log.info("Listando produtos...");
        return ResponseEntity.ok(consultaService.listar(pageable));
    }

    /**
     * Pesquisa um produto por {@code id}. Retorna um {@linkplain ProdutoDetalhesDto produto detalhado}.
     *
     * @param id o id do produto a ser consultado.
     *
     * @return um produto encontrado pelo {@code id}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDetalhesDto> consultarPorId(@PathVariable Long id) {
        log.info("Procurando produto com id {}...", id);
        return ResponseEntity.ok(consultaService.consultarPorId(id));
    }

    /**
     * Pesquisa produtos pelo {@code nome}.
     * Retorna uma {@linkplain Page sublista} de {@linkplain ProdutoDto produtos}.
     *
     * @param nome     o nome do(s) produto(s).
     * @param pageable o pageable padrão.
     *
     * @return uma sublista de uma lista com todos os produtos encontrados pelo {@code nome}.
     */
    @GetMapping("/nome")
    public ResponseEntity<Page<ProdutoDto>> consultarPorNome(
            @RequestParam @Size(message = "O nome deve ter pelo menos 2 caracteres", min = 2) String nome,
            @PageableDefault(sort = "nome", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
        log.info("Procurando produto com nome {}...", nome.trim());
        return ResponseEntity.ok(consultaService.consultarPorNome(nome.trim(), pageable));
    }

    /**
     * Pesquisa produtos pelo {@code id} da subcategoria.
     * Retorna uma {@linkplain Page sublista} de {@linkplain ProdutoDto produtos}.
     *
     * @param id       o id da subcategoria.
     * @param pageable o pageable padrão.
     *
     * @return uma sublista de uma lista com todos os produtos encontrados pelo {@code id} da subcategoria.
     */
    @GetMapping("/subcategoria/{id}")
    public ResponseEntity<Page<ProdutoDto>> consultarPorSubcategoria(@PathVariable Long id,
            @PageableDefault(sort = "nome", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
        log.info("Procurando produto com subcategoria com id {}...", id);
        return ResponseEntity.ok(consultaService.consultarPorSubcategoria(id, pageable));
    }

    /**
     * Pesquisa produtos pelo {@code id} da categoria.
     * Retorna uma {@linkplain Page sublista} de {@linkplain ProdutoDto produtos}.
     *
     * @param id       o id da categoria.
     * @param pageable o pageable padrão.
     *
     * @return uma sublista de uma lista com todos os produtos encontrados pelo {@code id} da categoria.
     */
    @GetMapping("/categoria/{id}")
    public ResponseEntity<Page<ProdutoDto>> consultarPorCategoria(@PathVariable Long id,
            @PageableDefault(sort = "nome", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
        log.info("Procurando produto com categoria com id {}...", id);
        return ResponseEntity.ok(consultaService.consultarPorCategoria(id, pageable));
    }

}
