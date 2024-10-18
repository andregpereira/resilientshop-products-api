package com.github.andregpereira.resilientshop.productsapi.infra.repositories;

import com.github.andregpereira.resilientshop.productsapi.infra.entities.CategoriaEntity;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.ProdutoEntity;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.SubcategoriaEntity;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;

import static com.github.andregpereira.resilientshop.productsapi.util.constant.CommonConstants.PAGEABLE_ID;
import static com.github.andregpereira.resilientshop.productsapi.util.constant.CommonConstants.PAGEABLE_NOME;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.ProdutoMockFactory.getProdutoEntity;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.ProdutoMockFactory.getProdutoEntityInvalido;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
class ProdutoRepositoryTest {

    private static final ProdutoEntity PRODUTO = getProdutoEntity();

    private static final CategoriaEntity CATEGORIA = PRODUTO.getCategoria();

    private static final SubcategoriaEntity SUBCATEGORIA = PRODUTO.getSubcategoria();

    static {
        SUBCATEGORIA.setCategoria(CATEGORIA);
    }

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private TestEntityManager em;

    @BeforeEach
    void beforeEach() {
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
        em.persist(PRODUTO);
    }

    @AfterEach
    void afterEach() {
        PRODUTO.setId(null);
        CATEGORIA.setId(null);
        SUBCATEGORIA.setId(null);
    }

    @Test
    void salvarProdutoComDadosValidosRetornaProduto() {
        final var id = repository
            .save(PRODUTO)
            .getId();

        final var sut = em.find(ProdutoEntity.class, id);

        then(sut)
            .isNotNull()
            .isEqualTo(PRODUTO);
    }

    @Test
    void salvarProdutoComDadosInvalidosThrowsException() {
        final ThrowingCallable sut = () -> repository.saveAndFlush(getProdutoEntityInvalido());

        assertThatThrownBy(sut).isExactlyInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void salvarProdutoComSkuExistenteThrowsException() {
        final var produto = em.find(ProdutoEntity.class, PRODUTO.getId());
        produto.setId(null);

        final ThrowingCallable sut = () -> repository.saveAndFlush(produto);

        assertThatThrownBy(sut).isExactlyInstanceOf(JpaSystemException.class);
    }

    @Test
    void listarProdutosExistentesRetornaPageProdutos() {
        final var sut = repository.findAll(PAGEABLE_ID);

        then(sut)
            .isNotEmpty()
            .hasSize(1);
    }

    @Test
    void listarProdutosInexistentesRetornaPageEmpty() {
        em.clear();

        final var sut = repository.findAll(PAGEABLE_ID);

        then(sut).isEmpty();
    }

    @Test
    void consultarProdutoPorIdExistenteRetornaTrueEProduto() {
        final var sut = repository.findById(PRODUTO.getId());

        then(repository.existsById(PRODUTO.getId())).isTrue();
        then(sut)
            .isNotEmpty()
            .get()
            .isEqualTo(PRODUTO);
    }

    @Test
    void consultarProdutoPorIdInexistenteRetornaFalseEEmpty() {
        em.clear();

        final var sut = repository.findById(10L);

        then(repository.existsById(10L)).isFalse();
        then(sut).isEmpty();
    }

    @Test
    void consultarProdutoPorNomeExistenteRetornaProduto() {
        final var sut = repository.findByName(PRODUTO.getNome(), PAGEABLE_NOME);

        then(sut)
            .isNotEmpty()
            .hasSize(1);
        then(sut
            .getContent()
            .getFirst()).isEqualTo(PRODUTO);
    }

    @Test
    void consultarProdutoPorNomeInexistenteRetornaPageEmpty() {
        em.clear();

        final var sut = repository.findByName("", PAGEABLE_NOME);

        then(sut).isEmpty();
    }

    @Test
    void consultarProdutosPorCategoriaExistenteRetornaPageProduto() {
        final var sut = repository.findAllByCategoriaId(CATEGORIA.getId(), PAGEABLE_NOME);

        then(sut)
            .isNotEmpty()
            .hasSize(1);
        then(sut
            .getContent()
            .getFirst()).isEqualTo(PRODUTO);
    }

    @Test
    void consultarProdutosPorCategoriaInexistenteRetornaPageEmpty() {
        final var sut = repository.findAllByCategoriaId(10L, PAGEABLE_NOME);

        then(sut).isEmpty();
    }

    @Test
    void consultarProdutosPorSubcategoriaExistenteRetornaPageProduto() {
        final var sut = repository.findAllBySubcategoriaId(SUBCATEGORIA.getId(), PAGEABLE_NOME);

        then(sut)
            .isNotEmpty()
            .hasSize(1);
        then(sut
            .getContent()
            .getFirst()).isEqualTo(PRODUTO);
    }

    @Test
    void consultarProdutosPorSubcategoriaInexistenteRetornaPageEmpty() {
        em.clear();

        final var sut = repository.findAllBySubcategoriaId(10L, PAGEABLE_NOME);

        then(sut).isEmpty();
    }

    @Test
    void removerProdutoPorIdExistenteRetornaNulo() {
        repository.deleteById(PRODUTO.getId());

        final var sut = em.find(ProdutoEntity.class, PRODUTO.getId());

        then(sut).isNull();
    }

}
