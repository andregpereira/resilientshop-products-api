package com.github.andregpereira.resilientshop.productsapi.repositories;

import com.github.andregpereira.resilientshop.productsapi.entities.Produto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import java.util.Optional;

import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaConstants.CATEGORIA;
import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaConstants.CATEGORIA_ATUALIZADA;
import static com.github.andregpereira.resilientshop.productsapi.constants.ProdutoConstants.*;
import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaConstants.SUBCATEGORIA;
import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaConstants.SUBCATEGORIA_ATUALIZADA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private TestEntityManager em;

    @AfterEach
    public void afterEach() {
        PRODUTO.setId(null);
        PRODUTO_ATUALIZADO.setId(null);
        SUBCATEGORIA.setId(null);
        SUBCATEGORIA_ATUALIZADA.setId(null);
        CATEGORIA.setId(null);
        CATEGORIA_ATUALIZADA.setId(null);
    }

    @Test
    void criarProdutoComDadosValidosRetornaProduto() {
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
        Produto produto = produtoRepository.save(PRODUTO);
        Produto sut = em.find(Produto.class, produto.getId());
        assertThat(sut).isNotNull();
        assertThat(sut.getSku()).isEqualTo(PRODUTO.getSku());
        assertThat(sut.getNome()).isEqualTo(PRODUTO.getNome());
        assertThat(sut.getDescricao()).isEqualTo(PRODUTO.getDescricao());
        assertThat(sut.getDataCriacao()).isEqualTo(PRODUTO.getDataCriacao());
        assertThat(sut.getValorUnitario()).isEqualTo(PRODUTO.getValorUnitario());
        assertThat(sut.getSubcategoria()).isEqualTo(PRODUTO.getSubcategoria());
        assertThat(sut.getSubcategoria().getCategoria()).isEqualTo(PRODUTO.getSubcategoria().getCategoria());
        assertThat(sut.getCategoria()).isEqualTo(PRODUTO.getCategoria());
    }

    @Test
    void criarProdutoComDadosInvalidosThrowsRuntimeException() {
        assertThatThrownBy(() -> produtoRepository.saveAndFlush(PRODUTO_VAZIO)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> produtoRepository.save(PRODUTO_INVALIDO)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void criarProdutoComSkuExistenteThrowsRuntimeException() {
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
        Produto sut = em.persistFlushFind(PRODUTO);
        sut.setId(null);
        assertThatThrownBy(() -> produtoRepository.saveAndFlush(sut)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void atualizarProdutoComDadosValidosRetornaProduto() {
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
        Produto produtoAntigo = em.persistFlushFind(PRODUTO);
        Produto produtoAtualizado = PRODUTO_ATUALIZADO;
        produtoAtualizado.setId(produtoAntigo.getId());
        produtoAtualizado.setSku(PRODUTO_ATUALIZADO.getSku());
        produtoAtualizado.setNome(PRODUTO_ATUALIZADO.getNome());
        produtoAtualizado.setDescricao(PRODUTO_ATUALIZADO.getDescricao());
        produtoAtualizado.setDataCriacao(PRODUTO_ATUALIZADO.getDataCriacao());
        produtoAtualizado.setValorUnitario(PRODUTO_ATUALIZADO.getValorUnitario());
        produtoAtualizado.getSubcategoria().setId(PRODUTO_ATUALIZADO.getSubcategoria().getId());
        produtoAtualizado.getSubcategoria().getCategoria().setId(
                PRODUTO_ATUALIZADO.getSubcategoria().getCategoria().getId());
        produtoAtualizado.getCategoria().setId(PRODUTO_ATUALIZADO.getCategoria().getId());
        em.persist(CATEGORIA_ATUALIZADA);
        em.persist(SUBCATEGORIA_ATUALIZADA);
        Produto sut = produtoRepository.save(produtoAtualizado);
        assertThat(sut).isNotNull();
        assertThat(sut.getId()).isEqualTo(produtoAntigo.getId());
        assertThat(sut.getSku()).isEqualTo(PRODUTO_ATUALIZADO.getSku());
        assertThat(sut.getNome()).isEqualTo(PRODUTO_ATUALIZADO.getNome());
        assertThat(sut.getDescricao()).isEqualTo(PRODUTO_ATUALIZADO.getDescricao());
        assertThat(sut.getDataCriacao()).isEqualTo(PRODUTO_ATUALIZADO.getDataCriacao());
        assertThat(sut.getValorUnitario()).isEqualTo(PRODUTO_ATUALIZADO.getValorUnitario());
        assertThat(sut.getSubcategoria()).isEqualTo(PRODUTO_ATUALIZADO.getSubcategoria());
        assertThat(sut.getSubcategoria().getCategoria()).isEqualTo(PRODUTO_ATUALIZADO.getSubcategoria().getCategoria());
        assertThat(sut.getCategoria()).isEqualTo(PRODUTO_ATUALIZADO.getCategoria());
    }

    @Test
    void atualizarProdutoComDadosInvalidosThrowsRuntimeException() {
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
        Produto produtoAntigo = em.persistFlushFind(PRODUTO);
        Produto sutVazio = PRODUTO_VAZIO;
        Produto sutInvalido = PRODUTO_INVALIDO;
        sutVazio.setId(produtoAntigo.getId());
        sutInvalido.setId(produtoAntigo.getId());
        assertThatThrownBy(() -> produtoRepository.saveAndFlush(sutVazio)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> produtoRepository.saveAndFlush(sutInvalido)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void consultarProdutoPorIdExistenteRetornaTrueEProduto() {
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
        Produto produto = em.persistFlushFind(PRODUTO);
        Optional<Produto> optionalProduto = produtoRepository.findById(produto.getId());
        assertThat(produtoRepository.existsById(produto.getId())).isTrue();
        assertThat(optionalProduto).isNotEmpty().get().isEqualTo(produto);
    }

    @Test
    void consultarProdutoPorIdInexistenteRetornaFalseEEmpty() {
        Optional<Produto> optionalProduto = produtoRepository.findById(10L);
        assertThat(produtoRepository.existsById(10L)).isFalse();
        assertThat(optionalProduto).isEmpty();
    }

    @Test
    void consultarProdutoPorNomeExistenteRetornaProduto() {
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
        Produto produto = em.persistFlushFind(PRODUTO);
        PageRequest pageable = PageRequest.of(0, 10, Direction.ASC, "nome");
        Page<Produto> pageProdutos = produtoRepository.findByNome(produto.getNome(), pageable);
        assertThat(pageProdutos).isNotEmpty().hasSize(1);
        assertThat(pageProdutos.getContent().get(0)).isEqualTo(produto);
    }

    @Test
    void consultarProdutoPorNomeInexistenteRetornaEmpty() {
        PageRequest pageable = PageRequest.of(0, 10, Direction.ASC, "nome");
        Page<Produto> pageProdutos = produtoRepository.findByNome("", pageable);
        assertThat(pageProdutos).isEmpty();
    }

    @Test
    void consultarProdutosPorSubcategoriaExistenteRetornaPageProduto() {
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
        Produto produto = em.persistFlushFind(PRODUTO);
        PageRequest pageable = PageRequest.of(0, 10, Direction.ASC, "nome");
        Page<Produto> pageProdutos = produtoRepository.findAllBySubcategoriaId(produto.getSubcategoria().getId(),
                pageable);
        assertThat(pageProdutos).isNotEmpty().hasSize(1);
        assertThat(pageProdutos.getContent().get(0)).isEqualTo(produto);
    }

    @Test
    void consultarProdutosPorSubcategoriaInexistenteRetornaEmpty() {
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
        Produto produto = em.persistFlushFind(PRODUTO);
        PageRequest pageable = PageRequest.of(0, 10, Direction.ASC, "nome");
        Page<Produto> pageProdutos = produtoRepository.findAllBySubcategoriaId(10L, pageable);
        assertThat(pageProdutos).isEmpty();
    }

    @Test
    void consultarProdutosPorCategoriaExistenteRetornaPageProduto() {
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
        Produto produto = em.persistFlushFind(PRODUTO);
        PageRequest pageable = PageRequest.of(0, 10, Direction.ASC, "nome");
        Page<Produto> pageProdutos = produtoRepository.findAllBySubcategoriaCategoriaId(
                produto.getSubcategoria().getCategoria().getId(), pageable);
        assertThat(pageProdutos).isNotEmpty().hasSize(1);
        assertThat(pageProdutos.getContent().get(0)).isEqualTo(produto);
    }

    @Test
    void consultarProdutosPorCategoriaInexistenteRetornaEmpty() {
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
        Produto produto = em.persistFlushFind(PRODUTO);
        PageRequest pageable = PageRequest.of(0, 10, Direction.ASC, "nome");
        Page<Produto> pageProdutos = produtoRepository.findAllBySubcategoriaCategoriaId(10L, pageable);
        assertThat(pageProdutos).isEmpty();
    }

    @Test
    void removerProdutoPorIdExistenteRetornaNulo() {
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
        Produto sut = em.persistFlushFind(PRODUTO);
        produtoRepository.deleteById(sut.getId());
        Produto produtoRemovido = em.find(Produto.class, sut.getId());
        assertThat(produtoRemovido).isNull();
    }

}

