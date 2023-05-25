package com.github.andregpereira.resilientshop.productsapi.app.services.produto;

import com.github.andregpereira.resilientshop.productsapi.app.dtos.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.produto.ProdutoDto;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.ProdutoNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.ProdutoMapper;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.Produto;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.CategoriaRepository;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.ProdutoRepository;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.SubcategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Classe de serviço de consulta de {@link Produto}.
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
    private final ProdutoRepository produtoRepository;

    /**
     * Injeção da dependência {@link ProdutoMapper} para realizar
     * conversões de entidade para DTO de produtos.
     */
    private final ProdutoMapper mapper;

    /**
     * Injeção da dependência {@link SubcategoriaRepository} para realizar operações de
     * consulta na tabela de subcategorias no banco de dados.
     */
    private final SubcategoriaRepository subcategoriaRepository;

    /**
     * Injeção da dependência {@link CategoriaRepository} para realizar operações de
     * consulta na tabela de categorias no banco de dados.
     */
    private final CategoriaRepository categoriaRepository;

    /**
     * Lista todos os {@linkplain Produto produtos} cadastrados.
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
        Page<Produto> produtos = produtoRepository.findAll(pageable);
        if (produtos.isEmpty()) {
            log.info("Não há produtos cadastrados");
            throw new ProdutoNotFoundException();
        }
        log.info("Retornando produtos");
        return produtos.map(mapper::toProdutoDto);
    }

    /**
     * Pesquisa um {@linkplain Produto produto} por {@code id}.
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
        return produtoRepository.findById(id).map(p -> {
            log.info("Retornando produto com id {}", id);
            return mapper.toProdutoDetalhesDto(p);
        }).orElseThrow(() -> {
            log.info("Produto não encontrado com id {}", id);
            return new ProdutoNotFoundException(id);
        });
    }

    /**
     * Pesquisa {@linkplain Produto produtos} por {@code nome}.
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
        Page<Produto> produtos = produtoRepository.findByNome(nome, pageable);
        if (produtos.isEmpty()) {
            log.info("Nenhum produto foi encontrado com o nome {}", nome);
            throw new ProdutoNotFoundException("nome", nome);
        }
        log.info("Retornando produto com nome {}", nome);
        return produtos.map(mapper::toProdutoDto);
    }

    /**
     * Pesquisa {@linkplain Produto produtos} pelo {@code id} da subcategoria.
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
        return subcategoriaRepository.findById(id).map(sc -> {
            Page<Produto> produtos = produtoRepository.findAllBySubcategoriaId(id, pageable);
            if (produtos.isEmpty()) {
                log.info("Nenhum produto foi encontrado com a subcategoria id {}", id);
                throw new ProdutoNotFoundException("subcategoria", sc.getNome());
            }
            log.info("Retornando produtos com subcategoria id {}", id);
            return produtos.map(mapper::toProdutoDto);
        }).orElseThrow(() -> {
            log.info("Nenhuma subcategoria foi encontrada com id {}", id);
            return new SubcategoriaNotFoundException(id);
        });
    }

    /**
     * Pesquisa {@linkplain Produto produtos} pelo {@code id} da categoria.
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
        return categoriaRepository.findById(id).map(c -> {
            Page<Produto> produtos = produtoRepository.findAllBySubcategoriaCategoriaId(id, pageable);
            if (produtos.isEmpty()) {
                log.info("Nenhum produto foi encontrado com a categoria id {}", id);
                throw new ProdutoNotFoundException("categoria", c.getNome());
            }
            log.info("Retornando produtos com categoria id {}", id);
            return produtos.map(mapper::toProdutoDto);
        }).orElseThrow(() -> {
            log.info("Nenhuma categoria foi encontrada com id {}", id);
            return new CategoriaNotFoundException(id);
        });
    }

}
