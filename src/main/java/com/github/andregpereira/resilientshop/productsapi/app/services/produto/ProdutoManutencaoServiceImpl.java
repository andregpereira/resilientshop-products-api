package com.github.andregpereira.resilientshop.productsapi.app.services.produto;

import com.github.andregpereira.resilientshop.productsapi.app.dtos.produto.ProdutoAtualizacaoDto;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.produto.ProdutoRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.Produto;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.ProdutoAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.ProdutoNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.ProdutoMapper;
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
        }
        Produto produto = mapper.toProduto(dto);
        produto.setDataCriacao(LocalDateTime.now());
        produto.setSubcategoria(subcategoriaRepository.getReferenceById(dto.idSubcategoria()));
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
        }
        Produto produtoAntigo = optionalProduto.get();
        Produto produtoAtualizado = mapper.toProduto(dto);
        produtoAtualizado.setId(id);
        produtoAtualizado.setSku(produtoAntigo.getSku());
        produtoAtualizado.setDataCriacao(produtoAntigo.getDataCriacao());
        produtoAtualizado.setSubcategoria(subcategoriaRepository.getReferenceById(dto.idSubcategoria()));
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
