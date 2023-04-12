package com.github.andregpereira.resilientshop.productsapi.services.produto;

import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoAtualizacaoDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Produto;
import com.github.andregpereira.resilientshop.productsapi.infra.exception.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.infra.exception.ProdutoAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.infra.exception.ProdutoNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.infra.exception.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.mappers.ProdutoMapper;
import com.github.andregpereira.resilientshop.productsapi.repositories.CategoriaRepository;
import com.github.andregpereira.resilientshop.productsapi.repositories.ProdutoRepository;
import com.github.andregpereira.resilientshop.productsapi.repositories.SubcategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class ProdutoManutencaoServiceImpl implements ProdutoManutencaoService {

    private final ProdutoRepository produtoRepository;

    private final ProdutoMapper mapper;

    private final SubcategoriaRepository subcategoriaRepository;

    private final CategoriaRepository categoriaRepository;

    public ProdutoDetalhesDto registrar(ProdutoRegistroDto dto) {
        if (produtoRepository.existsBySku(dto.sku())) {
            log.info("Produto já cadastrado com o SKU {}", dto.sku());
            throw new ProdutoAlreadyExistsException("Opa! Já existe um produto com esse SKU registrado");
        } else if (produtoRepository.existsByNome(dto.nome())) {
            log.info("Produto já cadastrado com o nome {}", dto.nome());
            throw new ProdutoAlreadyExistsException("Opa! Já existe um produto com esse nome registrado");
        } else if (!subcategoriaRepository.existsById(dto.idSubcategoria())) {
            log.info("Subcategoria não encontrada com id {}", dto.idSubcategoria());
            throw new SubcategoriaNotFoundException(
                    "Ops! Não foi possível encontrar uma subcategoria com o id " + dto.idSubcategoria());
        } else if (!categoriaRepository.existsById(dto.idCategoria())) {
            log.info("Categoria não encontrada com id {}", dto.idCategoria());
            throw new CategoriaNotFoundException(
                    "Poxa! Nenhuma categoria foi encontrada com o id " + dto.idCategoria());
        }
        Produto produto = mapper.toProduto(dto);
        produto.setDataCriacao(LocalDateTime.now());
        produto.setSubcategoria(subcategoriaRepository.getReferenceById(dto.idSubcategoria()));
        produto.setCategoria(categoriaRepository.getReferenceById(dto.idCategoria()));
        produto = produtoRepository.save(produto);
        log.info("Produto criado");
        return mapper.toProdutoDetalhesDto(produto);
    }

    public ProdutoDetalhesDto atualizar(Long id, ProdutoAtualizacaoDto dto) {
        Optional<Produto> optionalProduto = produtoRepository.findById(id);
        if (optionalProduto.isEmpty()) {
            log.info("Produto não encontrado com id {}", id);
            throw new ProdutoNotFoundException("Ops! Não foi possível encontrar um produto com o id " + id);
        } else if (produtoRepository.existsByNome(dto.nome())) {
            log.info("Produto já cadastrado com o nome {}", dto.nome());
            throw new ProdutoAlreadyExistsException("Opa! Já existe um produto com esse nome registrado");
        } else if (!subcategoriaRepository.existsById(dto.idSubcategoria())) {
            log.info("Subcategoria não encontrada com id {}", dto.idSubcategoria());
            throw new SubcategoriaNotFoundException(
                    "Ops! Não foi possível encontrar uma subcategoria com o id " + dto.idSubcategoria());
        } else if (!categoriaRepository.existsById(dto.idCategoria())) {
            log.info("Categoria não encontrada com id {}", dto.idCategoria());
            throw new CategoriaNotFoundException(
                    "Poxa! Nenhuma categoria foi encontrada com o id " + dto.idCategoria());
        }
        Produto produtoAntigo = optionalProduto.get();
        Produto produtoAtualizado = mapper.toProduto(dto);
        produtoAtualizado.setId(id);
        produtoAtualizado.setSku(produtoAntigo.getSku());
        produtoAtualizado.setDataCriacao(produtoAntigo.getDataCriacao());
        produtoAtualizado.setSubcategoria(subcategoriaRepository.getReferenceById(dto.idSubcategoria()));
        produtoAtualizado.setCategoria(categoriaRepository.getReferenceById(dto.idCategoria()));
        Produto produto = produtoRepository.save(produtoAtualizado);
        log.info("Produto com id {} atualizado", id);
        return mapper.toProdutoDetalhesDto(produto);
    }

    public String remover(Long id) {
        produtoRepository.findById(id).ifPresentOrElse(p -> produtoRepository.deleteById(id), () -> {
            log.info("Produto não encontrado com id {}", id);
            throw new ProdutoNotFoundException(
                    "Desculpe, não foi possível encontrar um produto com o id " + id + ". Verifique e tente novamente");
        });
        log.info("Produto com id {} removido", id);
        return "Produto removido";
    }

}
