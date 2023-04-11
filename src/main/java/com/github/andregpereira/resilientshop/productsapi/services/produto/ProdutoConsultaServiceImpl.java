package com.github.andregpereira.resilientshop.productsapi.services.produto;

import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.produto.ProdutoDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Produto;
import com.github.andregpereira.resilientshop.productsapi.infra.exception.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.infra.exception.ProdutoNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.infra.exception.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.mappers.ProdutoMapper;
import com.github.andregpereira.resilientshop.productsapi.repositories.CategoriaRepository;
import com.github.andregpereira.resilientshop.productsapi.repositories.ProdutoRepository;
import com.github.andregpereira.resilientshop.productsapi.repositories.SubcategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProdutoConsultaServiceImpl implements ProdutoConsultaService {

    private final ProdutoRepository produtoRepository;

    private final ProdutoMapper mapper;

    private final SubcategoriaRepository subcategoriaRepository;

    private final CategoriaRepository categoriaRepository;

    public Page<ProdutoDto> listar(Pageable pageable) {
        Page<Produto> produtos = produtoRepository.findAll(pageable);
        if (produtos.isEmpty()) {
            log.info("Não há produtos cadastrados");
            throw new ProdutoNotFoundException("Ops! Ainda não há produtos cadastrados");
        }
        log.info("Retornando produtos");
        return produtos.map(mapper::toProdutoDto);
    }

    public ProdutoDetalhesDto consultarPorId(Long id) {
        if (!produtoRepository.existsById(id)) {
            log.info("Produto não encontrado com id {}", id);
            throw new ProdutoNotFoundException(
                    "Desculpe, não foi possível encontrar um produto com o id " + id + ". Verifique e tente novamente");
        }
        log.info("Retornando produto com id {}", id);
        return mapper.toProdutoDetalhesDto(produtoRepository.getReferenceById(id));
    }

    public Page<ProdutoDto> consultarPorNome(String nome, Pageable pageable) {
        nome = nome.trim();
        Page<Produto> produtos = produtoRepository.findByNome(nome, pageable);
        if (produtos.isEmpty()) {
            log.info("Nenhum produto foi encontrado com o nome {}", nome);
            throw new ProdutoNotFoundException(
                    "Desculpe, não foi possível encontrar um produto com esse nome. Verifique e tente novamente");
        }
        log.info("Retornando produto com nome {}", nome);
        return produtos.map(mapper::toProdutoDto);
    }

    public Page<ProdutoDto> consultarPorSubcategoria(Long id, Pageable pageable) {
        if (!subcategoriaRepository.existsById(id)) {
            log.info("Nenhuma subcategoria foi encontrada com id {}", id);
            throw new SubcategoriaNotFoundException(
                    "Desculpe, não foi possível encontrar uma subcategoria com o id " + id + ". Verifique e tente novamente");
        }
        Page<Produto> produtos = produtoRepository.findAllBySubcategoriaId(id, pageable);
        if (produtos.isEmpty()) {
            log.info("Nenhum produto foi encontrado com a subcategoria id {}", id);
            throw new ProdutoNotFoundException("Ops! Nenhum produto foi encontrado com essa subcategoria");
        }
        log.info("Retornando produtos com subcategoria id {}", id);
        return produtos.map(mapper::toProdutoDto);
    }

    public Page<ProdutoDto> consultarPorCategoria(Long id, Pageable pageable) {
        if (!categoriaRepository.existsById(id)) {
            log.info("Nenhuma categoria foi encontrada com id {}", id);
            throw new CategoriaNotFoundException(
                    "Desculpe, não foi possível encontrar uma categoria com o id " + id + ". Verifique e tente novamente");
        }
        Page<Produto> produtos = produtoRepository.findAllBySubcategoriaCategoriaId(id, pageable);
        if (produtos.isEmpty()) {
            log.info("Nenhum produto foi encontrado com a categoria id {}", id);
            throw new ProdutoNotFoundException("Ops! Nenhum produto foi encontrado com essa categoria");
        }
        log.info("Retornando produtos com categoria id {}", id);
        return produtos.map(mapper::toProdutoDto);
    }

}
