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
            throw new ProdutoNotFoundException();
        }
        log.info("Retornando produtos");
        return produtos.map(mapper::toProdutoDto);
    }

    public ProdutoDetalhesDto consultarPorId(Long id) {
        return produtoRepository.findById(id).map(p -> {
            log.info("Retornando produto com id {}", id);
            return mapper.toProdutoDetalhesDto(p);
        }).orElseThrow(() -> {
            log.info("Produto não encontrado com id {}", id);
            return new ProdutoNotFoundException(id);
        });
    }

    public Page<ProdutoDto> consultarPorNome(String nome, Pageable pageable) {
        Page<Produto> produtos = produtoRepository.findByNome(nome, pageable);
        if (produtos.isEmpty()) {
            log.info("Nenhum produto foi encontrado com o nome {}", nome);
            throw new ProdutoNotFoundException(nome);
        }
        log.info("Retornando produto com nome {}", nome);
        return produtos.map(mapper::toProdutoDto);
    }

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
