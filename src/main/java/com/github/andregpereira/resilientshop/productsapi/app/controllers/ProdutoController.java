package com.github.andregpereira.resilientshop.productsapi.app.controllers;

import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoAtualizacaoDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoAtualizarEstoqueDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.app.services.produto.ProdutoConsultaService;
import com.github.andregpereira.resilientshop.productsapi.app.services.produto.ProdutoManutencaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Set;

/**
 * Controller de produtos da API de Produtos.
 *
 * @author André Garcia
 */
@RequiredArgsConstructor
@Slf4j
@Tag(
    name = "Produtos",
    description = "Operações de criação, atualização, desativação, reativação, subtração de estoque, retorno de estoque e consulta de produtos."
)
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
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<ProdutoDetalhesDto> criarProduto(@Valid @RequestBody ProdutoRegistroDto dto) {
        log.info("Criando produto...");
        ProdutoDetalhesDto produto = manutencaoService.criar(dto);
        log.info("Produto criado com sucesso");
        URI uri = UriComponentsBuilder
            .fromPath("/produtos/{id}")
            .buildAndExpand(produto.id())
            .toUri();
        return ResponseEntity
            .created(uri)
            .body(produto);
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
    @PutMapping(value = "/{id}")
    public ResponseEntity<ProdutoDetalhesDto> atualizarProduto(
        @PathVariable Long id, @Valid @RequestBody ProdutoAtualizacaoDto dto
    ) {
        log.info("Atualizando produto com id {}...", id);
        ProdutoDetalhesDto produto = manutencaoService.atualizar(id, dto);
        log.info("Produto com id {} atualizado", id);
        URI uri = UriComponentsBuilder
            .fromPath("/produtos/{id}")
            .buildAndExpand(id)
            .toUri();
        return ResponseEntity
            .ok()
            .location(uri)
            .body(produto);
    }

    /**
     * Desativa um produto por {@code id}.
     * Retorna uma mensagem de confirmação de desativação.
     *
     * @param id o id do produto a ser desativado.
     *
     * @return uma mensagem de confirmação de desativação.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> desativarProduto(@PathVariable Long id) {
        log.info("Desativando produto com id {}...", id);
        return ResponseEntity.ok(manutencaoService.desativar(id));
    }

    /**
     * Reativa um produto por {@code id}.
     * Retorna uma mensagem de confirmação de reativação.
     *
     * @param id o id do produto a ser reativado.
     *
     * @return uma mensagem de confirmação de reativação.
     */
    @PatchMapping("/reativar/{id}")
    public ResponseEntity<String> reativarProduto(@PathVariable Long id) {
        log.info("Reativando produto com id {}...", id);
        return ResponseEntity.ok(manutencaoService.reativar(id));
    }

    /**
     * Subtrai produtos do estoque, dado a lista de {@linkplain ProdutoAtualizarEstoqueDto produtos}.
     *
     * @param dtos a lista de produtos a terem seus estoques subtraídos.
     */
    @PutMapping("/estoque/subtrair")
    public ResponseEntity<Void> subtrairProdutosDoEstoque(@RequestBody Set<ProdutoAtualizarEstoqueDto> dtos) {
        log.info("Subtraindo produtos do estoque...");
        manutencaoService.subtrairEstoque(dtos);
        return ResponseEntity
            .ok()
            .build();
    }

    /**
     * Retorna produtos ao estoque, dado a lista de {@linkplain ProdutoAtualizarEstoqueDto produtos}.
     *
     * @param dtos a lista de produtos a serem retornados ao estoque.
     */
    @PutMapping("/estoque/retornar")
    public ResponseEntity<Void> retornarProdutosAoEstoque(@RequestBody Set<ProdutoAtualizarEstoqueDto> dtos) {
        log.info("Retornando produtos ao estoque...");
        manutencaoService.retornarEstoque(dtos);
        return ResponseEntity
            .ok()
            .build();
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
    public ResponseEntity<Page<ProdutoDto>> listarProdutos(
        @PageableDefault(sort = "id") Pageable pageable
    ) {
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
    @GetMapping(value = "/{id}")
    public ResponseEntity<ProdutoDetalhesDto> consultarProdutoPorId(@PathVariable Long id) {
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
    public ResponseEntity<Page<ProdutoDto>> consultarProdutosPorNome(
        @RequestParam @Size(message = "O nome deve ter pelo menos 2 caracteres", min = 2) String nome,
        @PageableDefault(sort = "nome") Pageable pageable
    ) {
        log.info("Procurando produto com nome {}...", nome.trim());
        return ResponseEntity.ok(consultaService.consultarPorNome(nome.trim(), pageable));
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
    public ResponseEntity<Page<ProdutoDto>> consultarProdutosPorIdCategoria(
        @PathVariable Long id, @PageableDefault(sort = "nome") Pageable pageable
    ) {
        log.info("Procurando produto com categoria com id {}...", id);
        return ResponseEntity.ok(consultaService.consultarPorCategoria(id, pageable));
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
    public ResponseEntity<Page<ProdutoDto>> consultarProdutosPorIdSubcategoria(
        @PathVariable Long id, @PageableDefault(sort = "nome") Pageable pageable
    ) {
        log.info("Procurando produto com subcategoria com id {}...", id);
        return ResponseEntity.ok(consultaService.consultarPorSubcategoria(id, pageable));
    }

}
