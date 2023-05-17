package com.github.andregpereira.resilientshop.productsapi.infra.repositories;

import com.github.andregpereira.resilientshop.productsapi.infra.entities.Categoria;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class CategoriaRepositoryTest {

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
        Categoria categoria = categoriaRepository.save(CATEGORIA);
        Categoria sut = em.find(Categoria.class, categoria.getId());
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
        Categoria sut = em.persistFlushFind(CATEGORIA);
        sut.setId(null);
        assertThatThrownBy(() -> categoriaRepository.saveAndFlush(sut)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void atualizarCategoriaComDadosValidosRetornaCategoria() {
        Categoria categoriaAntigo = em.persistFlushFind(CATEGORIA);
        Categoria categoriaAtualizado = CATEGORIA_ATUALIZADA;
        categoriaAtualizado.setId(categoriaAntigo.getId());
        categoriaAtualizado.setNome(CATEGORIA_ATUALIZADA.getNome());
        Categoria sut = categoriaRepository.save(categoriaAtualizado);
        assertThat(sut).isNotNull();
        assertThat(sut.getId()).isEqualTo(categoriaAntigo.getId());
        assertThat(sut.getNome()).isEqualTo(CATEGORIA_ATUALIZADA.getNome());
    }

    @Test
    void atualizarCategoriaComDadosInvalidosThrowsRuntimeException() {
        Categoria categoriaAntigo = em.persistFlushFind(CATEGORIA);
        Categoria sutVazio = CATEGORIA_VAZIA;
        Categoria sutInvalido = CATEGORIA_INVALIDA;
        sutVazio.setId(categoriaAntigo.getId());
        sutInvalido.setId(categoriaAntigo.getId());
        assertThatThrownBy(() -> categoriaRepository.saveAndFlush(sutVazio)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> categoriaRepository.saveAndFlush(sutInvalido)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void consultarCategoriaPorIdExistenteRetornaTrueECategoria() {
        Categoria categoria = em.persistFlushFind(CATEGORIA);
        Optional<Categoria> optionalCategoria = categoriaRepository.findById(categoria.getId());
        assertThat(categoriaRepository.existsById(categoria.getId())).isTrue();
        assertThat(optionalCategoria).isNotEmpty().get().isEqualTo(categoria);
    }

    @Test
    void consultarCategoriaPorIdInexistenteRetornaFalseEEmpty() {
        Optional<Categoria> optionalCategoria = categoriaRepository.findById(10L);
        assertThat(categoriaRepository.existsById(10L)).isFalse();
        assertThat(optionalCategoria).isEmpty();
    }

    @Test
    void removerCategoriaPorIdExistenteRetornaNulo() {
        Categoria sut = em.persistFlushFind(CATEGORIA);
        categoriaRepository.deleteById(sut.getId());
        Categoria categoriaRemovido = em.find(Categoria.class, sut.getId());
        assertThat(categoriaRemovido).isNull();
    }

}

