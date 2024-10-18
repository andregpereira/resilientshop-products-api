package com.github.andregpereira.resilientshop.productsapi.infra.repositories;

import com.github.andregpereira.resilientshop.productsapi.infra.entities.CategoriaEntity;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.config.PostgreSQLContainerConfig;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.config.PostgreSQLContainerConfig.PostgreSQLContainerInitializer;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;

import static com.github.andregpereira.resilientshop.productsapi.util.constant.CommonConstants.PAGEABLE_ID;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.CategoriaMockFactory.getCategoriaEntity;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.CategoriaMockFactory.getCategoriaEntityInvalida;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.InvalidObjectMockFactory.readInvalidJson;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
@ContextConfiguration(initializers = PostgreSQLContainerInitializer.class)
class CategoriaRepositoryPostgreSQLContainerTest extends PostgreSQLContainerConfig {

    private static final CategoriaEntity CATEGORIA = getCategoriaEntity();

    @Autowired
    private CategoriaRepository repository;

    @Autowired
    private TestEntityManager em;

    @BeforeEach
    void beforeEach() {
        em.persist(CATEGORIA);
    }

    @AfterEach
    void afterEach() {
        CATEGORIA.setId(null);
    }

    @Test
    void salvarCategoriaComDadosValidosRetornaCategoria() {
        final var id = repository
            .save(CATEGORIA)
            .getId();

        final var sut = em.find(CategoriaEntity.class, id);

        then(sut)
            .isNotNull()
            .isEqualTo(CATEGORIA);
    }

    @Test
    void salvarCategoriaComDadosInvalidosThrowsException() {
        final ThrowingCallable sut = () -> repository.saveAndFlush(getCategoriaEntityInvalida());

        assertThatThrownBy(sut).isExactlyInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void consultarCategoriaPorIdExistenteRetornaTrueECategoria() {
        final var sut = repository.findById(CATEGORIA.getId());

        then(repository.existsById(CATEGORIA.getId())).isTrue();
        then(sut)
            .get()
            .isEqualTo(CATEGORIA);
    }

    @Test
    void consultarCategoriaPorIdInexistenteRetornaFalseEEmpty() {
        final var sut = repository.findById(10L);

        then(repository.existsById(10L)).isFalse();
        then(sut).isEmpty();
    }

    @Test
    void consultarCategoriasExistentesRetornaPageCategoria() {
        final var sut = repository.findAll(PAGEABLE_ID);

        then(repository.existsById(CATEGORIA.getId())).isTrue();
        then(sut)
            .isNotEmpty()
            .hasSize(1)
            .containsExactlyInAnyOrder(CATEGORIA);
    }

    @Test
    void consultarCategoriaInexistentesRetornaPageEmpty() {
        em.clear();

        final var sut = repository.findAll(PAGEABLE_ID);

        then(sut).isEmpty();
    }

    @Test
    void removerCategoriaPorIdExistenteRetornaNulo() {
        repository.deleteById(CATEGORIA.getId());

        final var sut = em.find(CategoriaEntity.class, CATEGORIA.getId());

        then(sut).isNull();
    }

}
