package com.github.andregpereira.resilientshop.productsapi.services.produto;

import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoAtualizacaoDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Produto;
import com.github.andregpereira.resilientshop.productsapi.infra.exception.ProdutoAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.infra.exception.ProdutoNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.infra.exception.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.mappers.ProdutoMapper;
import com.github.andregpereira.resilientshop.productsapi.repositories.ProdutoRepository;
import com.github.andregpereira.resilientshop.productsapi.repositories.SubcategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class ProdutoManutencaoServiceImpl implements ProdutoManutencaoService {

    private final ProdutoRepository produtoRepository;

    private final ProdutoMapper mapper;

    private final SubcategoriaRepository subcategoriaRepository;

    public ProdutoDetalhesDto registrar(ProdutoRegistroDto dto) {
        if (produtoRepository.existsBySku(dto.sku())) {
            throw new ProdutoAlreadyExistsException("Opa! Já existe um produto com esse SKU registrado");
        } else if (produtoRepository.existsByNome(dto.nome())) {
            throw new ProdutoAlreadyExistsException("Opa! Já existe um produto com esse nome registrado");
        } else if (!subcategoriaRepository.existsById(dto.idSubcategoria())) {
            throw new SubcategoriaNotFoundException(
                    "Desculpe, não foi possível encontrar uma subcategoria com o id " + dto.idSubcategoria() + ". Verifique e tente novamente");
        }
        Produto produto = mapper.toProduto(dto);
        produto.setDataCriacao(LocalDateTime.now());
        produto.setSubcategoria(subcategoriaRepository.getReferenceById(dto.idSubcategoria()));
        return mapper.toProdutoDetalhesDto(produtoRepository.save(produto));
    }

    public ProdutoDetalhesDto atualizar(Long id, ProdutoAtualizacaoDto dto) {
        Optional<Produto> optionalProduto = produtoRepository.findById(id);
        if (optionalProduto.isEmpty()) {
            throw new ProdutoNotFoundException(
                    "Desculpe, não foi possível encontrar um produto com o id " + id + ". Verifique e tente novamente");
        } else if (produtoRepository.existsByNome(dto.nome())) {
            throw new ProdutoAlreadyExistsException("Opa! Já existe um produto com esse nome registrado");
        } else if (!subcategoriaRepository.existsById(dto.idSubcategoria())) {
            throw new SubcategoriaNotFoundException(
                    "Desculpe, não foi possível encontrar uma subcategoria com o id " + dto.idSubcategoria() + ". Verifique e tente novamente");
        }
        Produto produtoAntigo = optionalProduto.get();
        Produto produtoAtualizado = mapper.toProduto(dto);
        produtoAtualizado.setId(id);
        produtoAtualizado.setSku(produtoAntigo.getSku());
        produtoAtualizado.setDataCriacao(produtoAntigo.getDataCriacao());
        produtoAtualizado.setSubcategoria(subcategoriaRepository.getReferenceById(dto.idSubcategoria()));
        return mapper.toProdutoDetalhesDto(produtoRepository.save(produtoAtualizado));
    }

    public String remover(Long id) {
        produtoRepository.findById(id).ifPresentOrElse(p -> produtoRepository.deleteById(id), () -> {
            throw new ProdutoNotFoundException(
                    "Desculpe, não foi possível encontrar um produto com o id " + id + ". Verifique e tente novamente");
        });
        return "Produto removido.";
    }

}
