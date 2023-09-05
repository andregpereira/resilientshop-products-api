package com.github.andregpereira.resilientshop.productsapi.infra.repositories;

import com.github.andregpereira.resilientshop.productsapi.infra.entities.ProdutoEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.math.BigDecimal;
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
    private ProdutoRepository repository;

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
        ProdutoEntity produto = repository.save(PRODUTO);
        ProdutoEntity sut = em.find(ProdutoEntity.class, produto.getId());
        assertThat(sut).isNotNull();
        assertThat(sut.getSku()).isEqualTo(PRODUTO.getSku());
        assertThat(sut.getNome()).isEqualTo(PRODUTO.getNome());
        assertThat(sut.getDescricao()).isEqualTo(PRODUTO.getDescricao());
        assertThat(sut.getDataCriacao()).isEqualTo(PRODUTO.getDataCriacao());
        assertThat(sut.getValorUnitario()).isEqualTo(PRODUTO.getValorUnitario());
        assertThat(sut.getSubcategoria()).isEqualTo(PRODUTO.getSubcategoria());
        assertThat(sut.getSubcategoria().getCategoria()).isEqualTo(PRODUTO.getSubcategoria().getCategoria());
    }

    @Test
    void criarProdutoComDadosInvalidosThrowsRuntimeException() {
        assertThatThrownBy(() -> repository.saveAndFlush(PRODUTO_VAZIO)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> repository.save(PRODUTO_INVALIDO)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void criarProdutoComSkuExistenteThrowsRuntimeException() {
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
        ProdutoEntity sut = em.persistFlushFind(PRODUTO);
        sut.setId(null);
        assertThatThrownBy(() -> repository.saveAndFlush(sut)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void atualizarProdutoComDadosValidosRetornaProduto() {
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
        ProdutoEntity produtoAntigo = em.persistFlushFind(PRODUTO);
        ProdutoEntity produtoAtualizado = PRODUTO_ATUALIZADO;
        produtoAtualizado.setId(produtoAntigo.getId());
        produtoAtualizado.setSku(PRODUTO_ATUALIZADO.getSku());
        produtoAtualizado.setNome(PRODUTO_ATUALIZADO.getNome());
        produtoAtualizado.setDescricao(PRODUTO_ATUALIZADO.getDescricao());
        produtoAtualizado.setDataCriacao(PRODUTO_ATUALIZADO.getDataCriacao());
        produtoAtualizado.setValorUnitario(PRODUTO_ATUALIZADO.getValorUnitario());
        produtoAtualizado.getSubcategoria().setId(PRODUTO_ATUALIZADO.getSubcategoria().getId());
        produtoAtualizado.getSubcategoria().getCategoria().setId(
                PRODUTO_ATUALIZADO.getSubcategoria().getCategoria().getId());
        em.persist(CATEGORIA_ATUALIZADA);
        em.persist(SUBCATEGORIA_ATUALIZADA);
        ProdutoEntity sut = repository.save(produtoAtualizado);
        assertThat(sut).isNotNull();
        assertThat(sut.getId()).isEqualTo(produtoAntigo.getId());
        assertThat(sut.getSku()).isEqualTo(PRODUTO_ATUALIZADO.getSku());
        assertThat(sut.getNome()).isEqualTo(PRODUTO_ATUALIZADO.getNome());
        assertThat(sut.getDescricao()).isEqualTo(PRODUTO_ATUALIZADO.getDescricao());
        assertThat(sut.getDataCriacao()).isEqualTo(PRODUTO_ATUALIZADO.getDataCriacao());
        assertThat(sut.getValorUnitario()).isEqualTo(PRODUTO_ATUALIZADO.getValorUnitario());
        assertThat(sut.getSubcategoria()).isEqualTo(PRODUTO_ATUALIZADO.getSubcategoria());
        assertThat(sut.getSubcategoria().getCategoria()).isEqualTo(PRODUTO_ATUALIZADO.getSubcategoria().getCategoria());
    }

    @Test
    void atualizarProdutoComDadosInvalidosThrowsRuntimeException() {
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
        ProdutoEntity produtoAntigo = em.persistFlushFind(PRODUTO);
        ProdutoEntity sutVazio = PRODUTO_VAZIO;
        ProdutoEntity sutInvalido = PRODUTO_INVALIDO;
        sutVazio.setId(produtoAntigo.getId());
        sutInvalido.setId(produtoAntigo.getId());
        assertThatThrownBy(() -> repository.saveAndFlush(sutVazio)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> repository.saveAndFlush(sutInvalido)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void listarProdutosExistentesRetornaProdutos() {
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
        em.persistFlushFind(PRODUTO);
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
        ProdutoEntity sut2 = new ProdutoEntity(null, 1234567890L, "nome2", "Teste da classe Produto", LOCAL_DATE_TIME,
                BigDecimal.valueOf(10.99), 10, SUBCATEGORIA);
        em.persist(sut2);
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        Page<ProdutoEntity> pageProdutos = repository.findAll(pageable);
        assertThat(pageProdutos).isNotEmpty().hasSize(2);
    }

    @Test
    void listarProdutosInexistentesRetornaEmpty() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        Page<ProdutoEntity> pageProdutos = repository.findAll(pageable);
        assertThat(pageProdutos).isEmpty();
    }

    @Test
    void consultarProdutoPorIdExistenteRetornaTrueEProduto() {
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
        ProdutoEntity produto = em.persistFlushFind(PRODUTO);
        Optional<ProdutoEntity> optionalProduto = repository.findById(produto.getId());
        assertThat(repository.existsById(produto.getId())).isTrue();
        assertThat(optionalProduto).isNotEmpty().get().isEqualTo(produto);
    }

    @Test
    void consultarProdutoPorIdInexistenteRetornaFalseEEmpty() {
        Optional<ProdutoEntity> optionalProduto = repository.findById(10L);
        assertThat(repository.existsById(10L)).isFalse();
        assertThat(optionalProduto).isEmpty();
    }

    @Test
    void consultarProdutoPorNomeExistenteRetornaProduto() {
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
        ProdutoEntity produto = em.persistFlushFind(PRODUTO);
        PageRequest pageable = PageRequest.of(0, 10, Direction.ASC, "nome");
        Page<ProdutoEntity> pageProdutos = repository.findByName(produto.getNome(), pageable);
        assertThat(pageProdutos).isNotEmpty().hasSize(1);
        assertThat(pageProdutos.getContent().get(0)).isEqualTo(produto);
    }

    @Test
    void consultarProdutoPorNomeInexistenteRetornaEmpty() {
        PageRequest pageable = PageRequest.of(0, 10, Direction.ASC, "nome");
        Page<ProdutoEntity> pageProdutos = repository.findByName("", pageable);
        assertThat(pageProdutos).isEmpty();
    }

    @Test
    void consultarProdutosPorSubcategoriaExistenteRetornaPageProduto() {
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
        ProdutoEntity produto = em.persistFlushFind(PRODUTO);
        PageRequest pageable = PageRequest.of(0, 10, Direction.ASC, "nome");
        Page<ProdutoEntity> pageProdutos = repository.findAllBySubcategoriaId(produto.getSubcategoria().getId(), pageable);
        assertThat(pageProdutos).isNotEmpty().hasSize(1);
        assertThat(pageProdutos.getContent().get(0)).isEqualTo(produto);
    }

    @Test
    void consultarProdutosPorSubcategoriaInexistenteRetornaEmpty() {
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
        ProdutoEntity produto = em.persistFlushFind(PRODUTO);
        PageRequest pageable = PageRequest.of(0, 10, Direction.ASC, "nome");
        Page<ProdutoEntity> pageProdutos = repository.findAllBySubcategoriaId(10L, pageable);
        assertThat(pageProdutos).isEmpty();
    }

    @Test
    void consultarProdutosPorCategoriaExistenteRetornaPageProduto() {
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
        ProdutoEntity produto = em.persistFlushFind(PRODUTO);
        PageRequest pageable = PageRequest.of(0, 10, Direction.ASC, "nome");
        Page<ProdutoEntity> pageProdutos = repository.findAllByCategoriaId(
                produto.getSubcategoria().getCategoria().getId(), pageable);
        assertThat(pageProdutos).isNotEmpty().hasSize(1);
        assertThat(pageProdutos.getContent().get(0)).isEqualTo(produto);
    }

    @Test
    void consultarProdutosPorCategoriaInexistenteRetornaEmpty() {
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
        ProdutoEntity produto = em.persistFlushFind(PRODUTO);
        PageRequest pageable = PageRequest.of(0, 10, Direction.ASC, "nome");
        Page<ProdutoEntity> pageProdutos = repository.findAllByCategoriaId(10L, pageable);
        assertThat(pageProdutos).isEmpty();
    }

    @Test
    void removerProdutoPorIdExistenteRetornaNulo() {
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
        ProdutoEntity sut = em.persistFlushFind(PRODUTO);
        repository.deleteById(sut.getId());
        ProdutoEntity produtoRemovido = em.find(ProdutoEntity.class, sut.getId());
        assertThat(produtoRemovido).isNull();
    }

}

