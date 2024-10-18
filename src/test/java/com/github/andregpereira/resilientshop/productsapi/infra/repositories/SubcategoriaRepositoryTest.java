package com.github.andregpereira.resilientshop.productsapi.infra.repositories;

import com.github.andregpereira.resilientshop.productsapi.infra.entities.CategoriaEntity;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.SubcategoriaEntity;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import static com.github.andregpereira.resilientshop.productsapi.util.constant.CommonConstants.PAGEABLE_ID;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.CategoriaMockFactory.getCategoriaEntity;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.SubcategoriaMockFactory.getSubcategoriaEntity;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.SubcategoriaMockFactory.getSubcategoriaEntityInvalida;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
class SubcategoriaRepositoryTest {

    private static final CategoriaEntity CATEGORIA = getCategoriaEntity();

    private static final SubcategoriaEntity SUBCATEGORIA = getSubcategoriaEntity();

    static {
        SUBCATEGORIA.setCategoria(CATEGORIA);
    }

    @Autowired
    private SubcategoriaRepository repository;

    @Autowired
    private TestEntityManager em;

    @BeforeEach
    void beforeEach() {
        em.persist(CATEGORIA);
        em.persist(SUBCATEGORIA);
    }

    @AfterEach
    void afterEach() {
        CATEGORIA.setId(null);
        SUBCATEGORIA.setId(null);
    }

    @Test
    void salvarSubcategoriaComDadosValidosRetornaSubcategoria() {
        final var id = repository
            .save(SUBCATEGORIA)
            .getId();

        final var sut = em.find(SubcategoriaEntity.class, id);

        then(sut)
            .isNotNull()
            .isEqualTo(SUBCATEGORIA);
    }

    @Test
    void salvarSubcategoriaComDadosInvalidosThrowsRuntimeException() {
        final ThrowingCallable sut = () -> repository.saveAndFlush(getSubcategoriaEntityInvalida());

        assertThatThrownBy(sut).isExactlyInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void consultarSubcategoriaPorIdExistenteRetornaTrueESubcategoria() {
        final var sut = repository.findById(SUBCATEGORIA.getId());

        then(repository.existsById(SUBCATEGORIA.getId())).isTrue();
        then(sut)
            .isNotEmpty()
            .get()
            .isEqualTo(SUBCATEGORIA);
    }

    @Test
    void consultarSubcategoriaPorIdInexistenteRetornaFalseEEmpty() {
        final var sut = repository.findById(10L);

        then(repository.existsById(10L)).isFalse();
        then(sut).isEmpty();
    }

    @Test
    void consultarSubcategoriasExistentesRetornaPageSubcategoria() {
        final var sut = repository.findAll(PAGEABLE_ID);

        then(repository.existsById(SUBCATEGORIA.getId())).isTrue();
        then(sut)
            .isNotEmpty()
            .hasSize(1)
            .containsExactlyInAnyOrder(SUBCATEGORIA);
    }

    @Test
    void consultarSubcategoriasInexistentesRetornaPageEmpty() {
        em.clear();

        final var sut = repository.findAll(PAGEABLE_ID);

        then(sut).isEmpty();
    }

    @Test
    void removerSubcategoriaPorIdExistenteRetornaNulo() {
        repository.deleteById(SUBCATEGORIA.getId());

        final var sut = em.find(SubcategoriaEntity.class, SUBCATEGORIA.getId());

        then(sut).isNull();
    }

}
