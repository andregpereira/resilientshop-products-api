package com.github.andregpereira.resilientshop.productsapi.infra.repositories;

import com.github.andregpereira.resilientshop.productsapi.infra.entities.CategoriaEntity;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.config.PostgreSQLContainerConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ContextConfiguration(initializers = PostgreSQLContainerConfig.PostgreSQLContainerInitializer.class)
class CategoriaRepositoryPostgreSQLContainerTest extends PostgreSQLContainerConfig {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private TestEntityManager em;

    @AfterEach
    public void afterEach() {
        CATEGORIA.setId(null);
        CATEGORIA_ATUALIZADA.setId(null);
    }

    @Test
    void criarCategoriaComDadosValidosRetornaCategoria() {
        CategoriaEntity categoria = categoriaRepository.save(CATEGORIA);
        CategoriaEntity sut = em.find(CategoriaEntity.class, categoria.getId());
        assertThat(sut).isNotNull();
        assertThat(sut.getNome()).isEqualTo(CATEGORIA.getNome());
    }

    @Test
    void criarCategoriaComDadosInvalidosThrowsRuntimeException() {
        assertThatThrownBy(() -> categoriaRepository.saveAndFlush(CATEGORIA_VAZIA)).isInstanceOf(
                RuntimeException.class);
        assertThatThrownBy(() -> categoriaRepository.saveAndFlush(CATEGORIA_INVALIDA)).isInstanceOf(
                RuntimeException.class);
    }

    @Test
    void criarCategoriaComSkuExistenteThrowsRuntimeException() {
        CategoriaEntity sut = em.persistFlushFind(CATEGORIA);
        sut.setId(null);
        assertThatThrownBy(() -> categoriaRepository.saveAndFlush(sut)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void atualizarCategoriaComDadosValidosRetornaCategoria() {
        CategoriaEntity categoriaAntigo = em.persistFlushFind(CATEGORIA);
        CategoriaEntity categoriaAtualizado = CATEGORIA_ATUALIZADA;
        categoriaAtualizado.setId(categoriaAntigo.getId());
        categoriaAtualizado.setNome(CATEGORIA_ATUALIZADA.getNome());
        CategoriaEntity sut = categoriaRepository.save(categoriaAtualizado);
        assertThat(sut).isNotNull();
        assertThat(sut.getId()).isEqualTo(categoriaAntigo.getId());
        assertThat(sut.getNome()).isEqualTo(CATEGORIA_ATUALIZADA.getNome());
    }

    @Test
    void atualizarCategoriaComDadosInvalidosThrowsRuntimeException() {
        CategoriaEntity categoriaAntigo = em.persistFlushFind(CATEGORIA);
        CategoriaEntity sutVazio = CATEGORIA_VAZIA;
        CategoriaEntity sutInvalido = CATEGORIA_INVALIDA;
        sutVazio.setId(categoriaAntigo.getId());
        sutInvalido.setId(categoriaAntigo.getId());
        assertThatThrownBy(() -> categoriaRepository.saveAndFlush(sutVazio)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> categoriaRepository.saveAndFlush(sutInvalido)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void consultarCategoriaPorIdExistenteRetornaTrueECategoria() {
        CategoriaEntity categoria = em.persistFlushFind(CATEGORIA);
        Optional<CategoriaEntity> optionalCategoria = categoriaRepository.findById(categoria.getId());
        assertThat(categoriaRepository.existsById(categoria.getId())).isTrue();
        assertThat(optionalCategoria).isNotEmpty().get().isEqualTo(categoria);
    }

    @Test
    void consultarCategoriaPorIdInexistenteRetornaFalseEEmpty() {
        Optional<CategoriaEntity> optionalCategoria = categoriaRepository.findById(10L);
        assertThat(categoriaRepository.existsById(10L)).isFalse();
        assertThat(optionalCategoria).isEmpty();
    }

    @Test
    void removerCategoriaPorIdExistenteRetornaNulo() {
        CategoriaEntity sut = em.persistFlushFind(CATEGORIA);
        categoriaRepository.deleteById(sut.getId());
        CategoriaEntity categoriaRemovido = em.find(CategoriaEntity.class, sut.getId());
        assertThat(categoriaRemovido).isNull();
    }

}

