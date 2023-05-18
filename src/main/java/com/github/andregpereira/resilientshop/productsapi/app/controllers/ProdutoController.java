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
import java.util.List;

/**
 * Controller de produtos da API de Produtos
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
     * Cadastra um produto. Retorna um {@link ProdutoDetalhesDto}.
     *
     * @param dto        o produto a ser cadastrado.
     * @param uriBuilder
     *
     * @return Um {@link ProdutoDetalhesDto} com o produto criado.
     */
    @PostMapping
    public ResponseEntity<ProdutoDetalhesDto> criar(@RequestBody @Valid ProdutoRegistroDto dto,
            UriComponentsBuilder uriBuilder) {
        log.info("Criando produto...");
        ProdutoDetalhesDto produto = manutencaoService.registrar(dto);
        URI uri = uriBuilder.path("/produtos/{id}").buildAndExpand(produto.id()).toUri();
        log.info("Produto criado com sucesso");
        return ResponseEntity.created(uri).body(produto);
    }

    /**
     * Atualiza um produto por {@code id}. Retorna um {@link ProdutoDetalhesDto}.
     *
     * @param id  o {@code id} do produto a ser atualizado.
     * @param dto o produto a ser atualizado.
     *
     * @return Um {@link ProdutoDetalhesDto} com o produto atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDetalhesDto> atualizar(@PathVariable Long id,
            @RequestBody @Valid ProdutoAtualizacaoDto dto) {
        log.info("Atualizando produto com id {}...", id);
        return ResponseEntity.ok(manutencaoService.atualizar(id, dto));
    }

    /**
     * Remove um produto por {@code id}. Retorna uma {@link String}.
     *
     * @param id o {@code id} do produto a ser removido.
     *
     * @return Uma {@link String} com uma mensagem de confirmação de remoção.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> remover(@PathVariable Long id) {
        log.info("Removendo produto com id {}...", id);
        return ResponseEntity.ok(manutencaoService.remover(id));
    }

    /**
     * Subtrai produtos do estoque.
     *
     * @param dtos a lista de produtos a terem seus estoques subtraídos.
     */
    @PutMapping("/estoque/subtrair")
    public void subtrairEstoque(@RequestBody List<ProdutoAtualizarEstoqueDto> dtos) {
        log.info("Subtraindo estoque...");
        manutencaoService.subtrairEstoque(dtos);
    }

    /**
     * Retorna produtos ao estoque.
     *
     * @param dtos a lista de produtos a serem retornados ao estoque.
     */
    @PutMapping("/estoque/retornar")
    public void retornarEstoque(@RequestBody List<ProdutoAtualizarEstoqueDto> dtos) {
        log.info("Retornando estoque...");
        manutencaoService.retornarEstoque(dtos);
    }

    /**
     * Lista todos os produtos cadastrados. Retorna uma {@link Page} de {@link ProdutoDto}.
     *
     * @param pageable o {@code pageable} padrão.
     *
     * @return Uma {@link Page} de {@link ProdutoDto} com todos os produtos cadastrados.
     */
    @GetMapping
    public ResponseEntity<Page<ProdutoDto>> listar(
            @PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
        log.info("Listando produtos...");
        return ResponseEntity.ok(consultaService.listar(pageable));
    }

    /**
     * Pesquisa um produto por {@code id}. Retorna um {@link ProdutoDetalhesDto}.
     *
     * @param id o {@code id} do produto a ser consultado.
     *
     * @return Um {@link ProdutoDetalhesDto} com o produto encontrado pelo {@code id}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDetalhesDto> consultarPorId(@PathVariable Long id) {
        log.info("Procurando produto com id {}...", id);
        return ResponseEntity.ok(consultaService.consultarPorId(id));
    }

    /**
     * Pesquisa produtos pelo {@code nome}. Retorna uma {@link Page} de {@link ProdutoDto}.
     *
     * @param nome     o {@code nome} do produto.
     * @param pageable o {@code pageable} padrão.
     *
     * @return Uma {@link Page} de {@link ProdutoDto} com todos os produtos encontrados pelo {@code nome}.
     */
    @GetMapping("/nome")
    public ResponseEntity<Page<ProdutoDto>> consultarPorNome(
            @RequestParam @Size(message = "O nome deve ter pelo menos 2 caracteres", min = 2) String nome,
            @PageableDefault(sort = "nome", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
        log.info("Procurando produto com nome {}...", nome.trim());
        return ResponseEntity.ok(consultaService.consultarPorNome(nome.trim(), pageable));
    }

    /**
     * Pesquisa produtos pelo {@code id} da subcategoria. Retorna uma {@link Page} de {@link ProdutoDto}.
     *
     * @param id       o {@code id} da subcategoria.
     * @param pageable o {@code pageable} padrão.
     *
     * @return Uma {@link Page} de {@link ProdutoDto} com todos os produtos encontrados pelo {@code id} da subcategoria.
     */
    @GetMapping("/subcategoria/{id}")
    public ResponseEntity<Page<ProdutoDto>> consultarPorSubcategoria(@PathVariable Long id,
            @PageableDefault(sort = "nome", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
        log.info("Procurando produto com subcategoria com id {}...", id);
        return ResponseEntity.ok(consultaService.consultarPorSubcategoria(id, pageable));
    }

    /**
     * Pesquisa produtos pelo {@code id} da categoria. Retorna uma {@link Page} de {@link ProdutoDto}.
     *
     * @param id       o {@code id} da categoria.
     * @param pageable o {@code pageable} padrão.
     *
     * @return Uma {@link Page} de {@link ProdutoDto} com todos os produtos encontrados pelo {@code id} da categoria.
     */
    @GetMapping("/categoria/{id}")
    public ResponseEntity<Page<ProdutoDto>> consultarPorCategoria(@PathVariable Long id,
            @PageableDefault(sort = "nome", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
        log.info("Procurando produto com categoria com id {}...", id);
        return ResponseEntity.ok(consultaService.consultarPorCategoria(id, pageable));
    }

}
