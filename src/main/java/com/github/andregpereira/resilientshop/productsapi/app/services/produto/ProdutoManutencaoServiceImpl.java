package com.github.andregpereira.resilientshop.productsapi.app.services.produto;

import com.github.andregpereira.resilientshop.productsapi.app.dtos.produto.ProdutoAtualizacaoDto;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.produto.ProdutoAtualizarEstoqueDto;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.produto.ProdutoRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.ProdutoAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.ProdutoNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.ProdutoMapper;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.Produto;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.ProdutoRepository;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.SubcategoriaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

/**
 * Classe de serviço de manutenção de {@link Produto}.
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
        } else
            return subcategoriaRepository.findById(dto.idSubcategoria()).map(sc -> {
                Produto produto = mapper.toProduto(dto);
                produto.setSubcategoria(sc);
                return mapper.toProdutoDetalhesDto(produtoRepository.save(produto));
            }).orElseThrow(() -> {
                log.info("Subcategoria não encontrada com id {}", dto.idSubcategoria());
                return new SubcategoriaNotFoundException(dto.idSubcategoria());
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
        return produtoRepository.findById(id).map(p -> {
            if (produtoRepository.existsByNome(dto.nome())) {
                log.info("Produto já cadastrado com o nome {}", dto.nome());
                throw new ProdutoAlreadyExistsException(dto.nome());
            } else
                return subcategoriaRepository.findById(dto.idSubcategoria()).map(sc -> {
                    p.setNome(dto.nome());
                    p.setDescricao(dto.descricao());
                    p.setValorUnitario(dto.valorUnitario());
                    p.setEstoque(dto.estoque());
                    p.setSubcategoria(sc);
//                    Produto produtoAtualizado = mapper.toProduto(dto);
//                    produtoAtualizado.setId(id);
//                    produtoAtualizado.setSku(produtoAntigo.getSku());
//                    produtoAtualizado.setSubcategoria(sc);
//                    produtoRepository.save(produtoAntigo);
//                    return mapper.toProdutoDetalhesDto(produtoAntigo);
                    return p;
                }).map(produtoRepository::save).map(mapper::toProdutoDetalhesDto).orElseThrow(() -> {
                    log.info("Subcategoria não encontrada com id {}", dto.idSubcategoria());
                    return new SubcategoriaNotFoundException(dto.idSubcategoria());
                });
        }).orElseThrow(() -> {
            log.info("Produto não encontrado com id {}", id);
            return new ProdutoNotFoundException(id);
        });
    }

    /**
     * Remove um {@linkplain Produto produto} por {@code id}.
     * Retorna uma mensagem de confirmação de remoção.
     *
     * @param id o id do produto a ser removido.
     *
     * @return uma mensagem de confirmação de remoção.
     *
     * @throws ProdutoNotFoundException caso o produto não seja encontrado.
     */
    @Override
    public String remover(Long id) {
        return produtoRepository.findById(id).map(p -> {
            produtoRepository.deleteById(id);
            log.info("Produto com id {} removido", id);
            return MessageFormat.format("Produto com id {0} removido com sucesso", id);
        }).orElseThrow(() -> {
            log.info("Produto não encontrado com id {}", id);
            return new ProdutoNotFoundException(id);
        });
    }

    /**
     * Subtrai {@linkplain Produto produtos} do estoque, dado a lista de {@linkplain ProdutoAtualizarEstoqueDto produtos}.
     *
     * @param dtos a lista de produtos a terem seus estoques subtraídos.
     *
     * @throws ProdutoNotFoundException caso o produto não seja encontrado ou o estoque é insuficiente.
     */
    @Override
    public void subtrairEstoque(List<ProdutoAtualizarEstoqueDto> dtos) {
        dtos.stream().forEach(pDto -> produtoRepository.findById(pDto.id()).ifPresentOrElse(p -> {
            if (p.getEstoque() < pDto.quantidade())
                throw new ProdutoNotFoundException(
                        MessageFormat.format("Ops! O produto {0} não possui estoque suficiente", p.getNome()));
            p.setEstoque(p.getEstoque() - pDto.quantidade());
            produtoRepository.save(p);
            log.info("Produto {} com {} itens subtraídos do estoque. Estoque atual: {}", p.getNome(), pDto.quantidade(),
                    p.getEstoque());
        }, () -> {
            log.info("Produto não encontrado com id {}", pDto.id());
            throw new ProdutoNotFoundException(pDto.id());
        }));
    }

    /**
     * Retorna {@linkplain Produto produtos} ao estoque, dado a lista de {@linkplain ProdutoAtualizarEstoqueDto produtos}.
     *
     * @param dtos a lista de produtos a serem retornados ao estoque.
     *
     * @throws ProdutoNotFoundException caso o produto não seja encontrado.
     */
    @Override
    public void retornarEstoque(List<ProdutoAtualizarEstoqueDto> dtos) {
        dtos.stream().forEach(pDto -> produtoRepository.findById(pDto.id()).ifPresentOrElse(p -> {
            p.setEstoque(p.getEstoque() + pDto.quantidade());
            produtoRepository.save(p);
            log.info("Produto {} com {} itens retornados ao estoque. Estoque atual: {}", p.getNome(), pDto.quantidade(),
                    p.getEstoque());
        }, () -> {
            log.info("Produto não encontrado com id {}", pDto.id());
            throw new ProdutoNotFoundException(pDto.id());
        }));
    }

}
