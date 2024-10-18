package com.github.andregpereira.resilientshop.productsapi.app.services.produto;

import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoAtualizacaoDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoAtualizarEstoqueDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.ProdutoAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.ProdutoNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.ProdutoMapper;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.ProdutoEntity;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.CategoriaRepository;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.ProdutoRepository;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.SubcategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Set;

/**
 * Classe de serviço de manutenção de {@link ProdutoEntity}.
 *
 * @author André Garcia
 * @see ProdutoManutencaoService
 */
@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class ProdutoManutencaoServiceImpl implements ProdutoManutencaoService {

    /**
     * Injeção da dependência {@link ProdutoRepository} para realizar operações de
     * manutenção na tabela de produtos no banco de dados.
     */
    private final ProdutoRepository produtoRepository;

    /**
     * Injeção da dependência {@link ProdutoMapper} para realizar
     * conversões de DTO e entidade de produtos.
     */
    private final ProdutoMapper mapper;

    /**
     * Injeção da dependência {@link CategoriaRepository} para realizar operações de
     * consulta na tabela de subcategorias no banco de dados.
     */
    private final CategoriaRepository categoriaRepository;

    /**
     * Injeção da dependência {@link SubcategoriaRepository} para realizar operações de
     * consulta na tabela de subcategorias no banco de dados.
     */
    private final SubcategoriaRepository subcategoriaRepository;

    /**
     * Cadastra um {@linkplain ProdutoRegistroDto produto}.
     * Retorna um {@linkplain ProdutoDetalhesDto produto detalhado}.
     *
     * @param dto o produto a ser cadastrado.
     *
     * @return o produto salvo no banco de dados.
     *
     * @throws ProdutoAlreadyExistsException caso exista um produto com o SKU ou o nome já cadastrados.
     * @throws SubcategoriaNotFoundException caso nenhuma subcategoria seja encontrada.
     */
    @Override
    public ProdutoDetalhesDto criar(ProdutoRegistroDto dto) {
        if (produtoRepository.existsBySku(dto.sku())) {
            log.info("Produto já cadastrado com o SKU {}", dto.sku());
            throw new ProdutoAlreadyExistsException(dto.sku());
        } else if (produtoRepository.existsByNome(dto.nome())) {
            log.info("Produto já cadastrado com o nome {}", dto.nome());
            throw new ProdutoAlreadyExistsException(dto.nome());
        }
        return categoriaRepository
            .findById(dto.categoriaId())
            .map(c -> {
                ProdutoEntity produto = mapper.toProduto(dto);
                produto.setCategoria(c);
                if (dto.subcategoriaId() != null) {
                    subcategoriaRepository
                        .findById(dto.subcategoriaId())
                        .ifPresent(produto::setSubcategoria);
                }
                return mapper.toProdutoDetalhesDto(produtoRepository.save(produto));
            })
            .orElseThrow(() -> {
                log.info("Categoria não encontrada com id {}", dto.categoriaId());
                return new CategoriaNotFoundException(dto.categoriaId());
            });
    }

    /**
     * Atualiza um {@linkplain ProdutoAtualizacaoDto produto} por {@code id}.
     * Retorna um {@linkplain ProdutoDetalhesDto produto detalhado}.
     *
     * @param id  o id do produto a ser atualizado.
     * @param dto os dados do produto a serem atualizados.
     *
     * @return o produto atualizado no banco de dados.
     *
     * @throws ProdutoNotFoundException      caso o produto não seja encontrado.
     * @throws ProdutoAlreadyExistsException caso exista um produto com o nome já cadastrados.
     * @throws SubcategoriaNotFoundException caso nenhuma subcategoria seja encontrada.
     */
    @Override
    public ProdutoDetalhesDto atualizar(Long id, ProdutoAtualizacaoDto dto) {
        return produtoRepository
            .findById(id)
            .map(p -> {
                if (produtoRepository.existsByNome(dto.nome())) {
                    log.info("Produto já cadastrado com o nome {}", dto.nome());
                    throw new ProdutoAlreadyExistsException(dto.nome());
                }
                return categoriaRepository
                    .findById(dto.categoriaId())
                    .map(c -> {
                        p.setNome(dto.nome());
                        p.setDescricao(dto.descricao());
                        p.setValorUnitario(dto.valorUnitario());
                        p.setEstoque(dto.estoque());
                        p.setImageUrl(dto.imageUrl());
                        p.setAtivo(dto.ativo());
                        p.setCategoria(c);
                        if (dto.subcategoriaId() != null) {
                            subcategoriaRepository
                                .findById(dto.subcategoriaId())
                                .ifPresent(p::setSubcategoria);
                        }
                        return mapper.toProdutoDetalhesDto(produtoRepository.save(p));
                    })
                    .orElseThrow(() -> {
                        log.info("Categoria não encontrada com id {}", dto.categoriaId());
                        return new CategoriaNotFoundException(dto.categoriaId());
                    });
            })
            .orElseThrow(() -> {
                log.info("Produto não encontrado com id {}", id);
                return new ProdutoNotFoundException(id);
            });
    }

    /**
     * Remove um {@linkplain ProdutoEntity produto} por {@code id}.
     * Retorna uma mensagem de confirmação de remoção.
     *
     * @param id o id do produto a ser removido.
     *
     * @return uma mensagem de confirmação de remoção.
     *
     * @throws ProdutoNotFoundException caso o produto não seja encontrado.
     */
    @Override
    public String desativar(Long id) {
        return produtoRepository
            .findById(id)
            .map(p -> {
                p.setAtivo(false);
                produtoRepository.save(p);
                log.info("Produto com id {} desativado", id);
                return MessageFormat.format("Produto com id {0} desativado com sucesso", id);
            })
            .orElseThrow(() -> {
                log.info("Produto ativo não encontrado com id {}", id);
                return new ProdutoNotFoundException(id, true);
            });
    }

    @Override
    public String reativar(Long id) {
        return produtoRepository
            .findById(id)
            .map(p -> {
                p.setAtivo(true);
                produtoRepository.save(p);
                log.info("Produto com id {} reativado", id);
                return MessageFormat.format("Produto com id {0} reativado com sucesso", id);
            })
            .orElseThrow(() -> {
                log.info("Produto inativo não encontrado com id {}", id);
                return new ProdutoNotFoundException(id, false);
            });
    }

    /**
     * Subtrai {@linkplain ProdutoEntity produtos} do estoque, dado a lista de {@linkplain ProdutoAtualizarEstoqueDto produtos}.
     *
     * @param dtos a lista de produtos a terem seus estoques subtraídos.
     *
     * @throws ProdutoNotFoundException caso o produto não seja encontrado ou o estoque é insuficiente.
     */
    @Override
    public void subtrairEstoque(Set<ProdutoAtualizarEstoqueDto> dtos) {
        dtos
            .stream()
            .forEach(dto -> produtoRepository
                .findById(dto.id())
                .ifPresentOrElse(p -> {
                    if (p.getEstoque() < dto.quantidade()) {
                        throw new ProdutoNotFoundException(MessageFormat.format(
                            "Ops! O produto {0} não possui estoque suficiente",
                            p.getNome()
                        ));
                    }
                    p.setEstoque(p.getEstoque() - dto.quantidade());
                    produtoRepository.save(p);
                    log.info(
                        "Produto {} com {} itens subtraídos do estoque. Estoque atual: {}",
                        p.getNome(),
                        dto.quantidade(),
                        p.getEstoque()
                    );
                }, () -> {
                    log.info("Produto não encontrado com id {}", dto.id());
                    throw new ProdutoNotFoundException(dto.id());
                }));
    }

    /**
     * Retorna {@linkplain ProdutoEntity produtos} ao estoque, dado a lista de {@linkplain ProdutoAtualizarEstoqueDto produtos}.
     *
     * @param dtos a lista de produtos a serem retornados ao estoque.
     *
     * @throws ProdutoNotFoundException caso o produto não seja encontrado.
     */
    @Override
    public void retornarEstoque(Set<ProdutoAtualizarEstoqueDto> dtos) {
        dtos
            .stream()
            .forEach(dto -> produtoRepository
                .findById(dto.id())
                .ifPresentOrElse(p -> {
                    p.setEstoque(p.getEstoque() + dto.quantidade());
                    produtoRepository.save(p);
                    log.info(
                        "Produto {} com {} itens retornados ao estoque. Estoque atual: {}",
                        p.getNome(),
                        dto.quantidade(),
                        p.getEstoque()
                    );
                }, () -> {
                    log.info("Produto não encontrado com id {}", dto.id());
                    throw new ProdutoNotFoundException(dto.id());
                }));
    }

}
