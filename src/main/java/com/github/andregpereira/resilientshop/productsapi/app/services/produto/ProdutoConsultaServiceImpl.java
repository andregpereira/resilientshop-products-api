package com.github.andregpereira.resilientshop.productsapi.app.services.produto;

import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoDto;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.ProdutoNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.ProdutoMapper;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.ProdutoEntity;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Classe de serviço de consulta de {@link ProdutoEntity}.
 *
 * @author André Garcia
 * @see ProdutoConsultaService
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class ProdutoConsultaServiceImpl implements ProdutoConsultaService {

    /**
     * Injeção da dependência {@link ProdutoRepository} para realizar operações de
     * consulta na tabela de produtos no banco de dados.
     */
    private final ProdutoRepository repository;

    /**
     * Injeção da dependência {@link ProdutoMapper} para realizar
     * conversões de entidade para DTO de produtos.
     */
    private final ProdutoMapper mapper;

    /**
     * Lista todos os {@linkplain ProdutoEntity produtos} cadastrados.
     * Retorna uma {@linkplain Page sublista} de {@linkplain ProdutoDto produtos}.
     *
     * @param pageable o pageable padrão.
     *
     * @return uma sublista de uma lista com todos os produtos cadastrados.
     *
     * @throws ProdutoNotFoundException caso nenhum produto seja encontrado.
     */
    @Override
    public Page<ProdutoDto> listar(Pageable pageable) {
        log.info("Retornando produtos");
        return repository.findAll(pageable).map(mapper::toProdutoDto);
    }

    /**
     * Pesquisa um {@linkplain ProdutoEntity produto} por {@code id}.
     * Retorna um {@linkplain  ProdutoDetalhesDto produto detalhado}.
     *
     * @param id o id do produto.
     *
     * @return um produto encontrado pelo {@code id}.
     *
     * @throws ProdutoNotFoundException caso o produto não seja encontrado.
     */
    @Override
    public ProdutoDetalhesDto consultarPorId(Long id) {
        log.info("Retornando produto com id {}", id);
        return repository.findById(id).map(mapper::toProdutoDetalhesDto).orElseThrow(() -> {
            log.info("Produto não encontrado com id {}", id);
            return new ProdutoNotFoundException(id);
        });
    }

    /**
     * Pesquisa {@linkplain ProdutoEntity produtos} por {@code nome}.
     * Retorna uma {@linkplain Page sublista} de {@linkplain ProdutoDto produtos}.
     *
     * @param nome     o nome do produto.
     * @param pageable o pageable padrão.
     *
     * @return uma sublista de uma lista de produtos encontrados pelo {@code nome}.
     *
     * @throws ProdutoNotFoundException caso nenhum produto seja encontrado.
     */
    @Override
    public Page<ProdutoDto> consultarPorNome(String nome, Pageable pageable) {
        log.info("Retornando produtos com nome {}", nome);
        return repository.findByName(nome, pageable).map(mapper::toProdutoDto);
    }

    /**
     * Pesquisa {@linkplain ProdutoEntity produtos} pelo {@code id} da subcategoria.
     * Retorna uma {@linkplain Page sublista} de {@linkplain ProdutoDto produtos}.
     *
     * @param id       o id da subcategoria.
     * @param pageable o pageable padrão.
     *
     * @return uma sublista de uma lista com todos os produtos encontrados pelo {@code id} da subcategoria.
     *
     * @throws ProdutoNotFoundException      caso nenhum produto seja encontrado.
     * @throws SubcategoriaNotFoundException caso nenhuma subcategoria seja encontrada.
     */
    @Override
    public Page<ProdutoDto> consultarPorSubcategoria(Long id, Pageable pageable) {
        log.info("Retornando produtos com subcategoria id {}", id);
        return repository.findAllBySubcategoriaId(id, pageable).map(mapper::toProdutoDto);
    }

    /**
     * Pesquisa {@linkplain ProdutoEntity produtos} pelo {@code id} da categoria.
     * Retorna uma {@linkplain Page sublista} de {@linkplain ProdutoDto produtos}.
     *
     * @param id       o id da categoria.
     * @param pageable o pageable padrão.
     *
     * @return uma sublista de uma lista com todos os produtos encontrados pelo {@code id} da categoria.
     *
     * @throws ProdutoNotFoundException   caso nenhum produto seja encontrado.
     * @throws CategoriaNotFoundException caso nenhuma categoria seja encontrada.
     */
    @Override
    public Page<ProdutoDto> consultarPorCategoria(Long id, Pageable pageable) {
        log.info("Retornando produtos com categoria id {}", id);
        return repository.findAllByCategoriaId(id, pageable).map(mapper::toProdutoDto);
    }

}
