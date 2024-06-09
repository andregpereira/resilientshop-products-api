package com.github.andregpereira.resilientshop.productsapi.infra.repositories;

import com.github.andregpereira.resilientshop.productsapi.infra.entities.SubcategoriaEntity;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.config.PostgreSQLContainerConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaConstants.CATEGORIA;
import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaConstants.CATEGORIA_ATUALIZADA;
import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ContextConfiguration(initializers = PostgreSQLContainerConfig.PostgreSQLContainerInitializer.class)
class SubcategoriaRepositoryPostgreSQLContainerTest extends PostgreSQLContainerConfig {

    @Autowired
    private SubcategoriaRepository subcategoriaRepository;

    @Autowired
    private TestEntityManager em;

    @AfterEach
    public void afterEach() {
        SUBCATEGORIA.setId(null);
        SUBCATEGORIA_ATUALIZADA.setId(null);
        CATEGORIA.setId(null);
        CATEGORIA_ATUALIZADA.setId(null);
    }

    @Test
    void criarSubcategoriaComDadosValidosRetornaSubcategoria() {
        em.persist(CATEGORIA);
        SubcategoriaEntity subcategoria = subcategoriaRepository.save(SUBCATEGORIA);
        SubcategoriaEntity sut = em.find(SubcategoriaEntity.class, subcategoria.getId());
        assertThat(sut).isNotNull();
        assertThat(sut.getNome()).isEqualTo(SUBCATEGORIA.getNome());
        assertThat(sut.getDescricao()).isEqualTo(SUBCATEGORIA.getDescricao());
        assertThat(sut.getCategoria()).isEqualTo(SUBCATEGORIA.getCategoria());
    }

    @Test
    void criarSubcategoriaComDadosInvalidosThrowsRuntimeException() {
        assertThatThrownBy(() -> subcategoriaRepository.saveAndFlush(SUBCATEGORIA_VAZIA)).isInstanceOf(
                RuntimeException.class);
        assertThatThrownBy(() -> subcategoriaRepository.save(SUBCATEGORIA_INVALIDA)).isInstanceOf(
                RuntimeException.class);
    }

    @Test
    void criarSubcategoriaComSkuExistenteThrowsRuntimeException() {
        em.persist(CATEGORIA);
        SubcategoriaEntity sut = em.persistFlushFind(SUBCATEGORIA);
        sut.setId(null);
        assertThatThrownBy(() -> subcategoriaRepository.saveAndFlush(sut)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void atualizarSubcategoriaComDadosValidosRetornaSubcategoria() {
        em.persist(CATEGORIA);
        SubcategoriaEntity subcategoriaAntigo = em.persistFlushFind(SUBCATEGORIA);
        SubcategoriaEntity subcategoriaAtualizado = SUBCATEGORIA_ATUALIZADA;
        subcategoriaAtualizado.setId(subcategoriaAntigo.getId());
        subcategoriaAtualizado.setNome(SUBCATEGORIA_ATUALIZADA.getNome());
        subcategoriaAtualizado.setDescricao(SUBCATEGORIA_ATUALIZADA.getDescricao());
        subcategoriaAtualizado.getCategoria().setId(SUBCATEGORIA_ATUALIZADA.getCategoria().getId());
        em.persist(CATEGORIA_ATUALIZADA);
        SubcategoriaEntity sut = subcategoriaRepository.save(subcategoriaAtualizado);
        assertThat(sut).isNotNull();
        assertThat(sut.getId()).isEqualTo(subcategoriaAntigo.getId());
        assertThat(sut.getNome()).isEqualTo(SUBCATEGORIA_ATUALIZADA.getNome());
        assertThat(sut.getDescricao()).isEqualTo(SUBCATEGORIA_ATUALIZADA.getDescricao());
        assertThat(sut.getCategoria()).isEqualTo(SUBCATEGORIA_ATUALIZADA.getCategoria());
    }

    @Test
    void atualizarSubcategoriaComDadosInvalidosThrowsRuntimeException() {
        em.persist(CATEGORIA);
        SubcategoriaEntity subcategoriaAntigo = em.persistFlushFind(SUBCATEGORIA);
        SubcategoriaEntity sutVazio = SUBCATEGORIA_VAZIA;
        SubcategoriaEntity sutInvalido = SUBCATEGORIA_INVALIDA;
        sutVazio.setId(subcategoriaAntigo.getId());
        sutInvalido.setId(subcategoriaAntigo.getId());
        assertThatThrownBy(() -> subcategoriaRepository.saveAndFlush(sutVazio)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> subcategoriaRepository.saveAndFlush(sutInvalido)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void consultarSubcategoriaPorIdExistenteRetornaTrueESubcategoria() {
        em.persist(CATEGORIA);
        SubcategoriaEntity subcategoria = em.persistFlushFind(SUBCATEGORIA);
        Optional<SubcategoriaEntity> optionalSubcategoria = subcategoriaRepository.findById(subcategoria.getId());
        assertThat(subcategoriaRepository.existsById(subcategoria.getId())).isTrue();
        assertThat(optionalSubcategoria).isNotEmpty().get().isEqualTo(subcategoria);
    }

    @Test
    void consultarSubcategoriaPorIdInexistenteRetornaFalseEEmpty() {
        Optional<SubcategoriaEntity> optionalSubcategoria = subcategoriaRepository.findById(10L);
        assertThat(subcategoriaRepository.existsById(10L)).isFalse();
        assertThat(optionalSubcategoria).isEmpty();
    }

    @Test
    void removerSubcategoriaPorIdExistenteRetornaNulo() {
        em.persist(CATEGORIA);
        SubcategoriaEntity sut = em.persistFlushFind(SUBCATEGORIA);
        subcategoriaRepository.deleteById(sut.getId());
        SubcategoriaEntity subcategoriaRemovido = em.find(SubcategoriaEntity.class, sut.getId());
        assertThat(subcategoriaRemovido).isNull();
    }

}

