package com.github.andregpereira.resilientshop.productsapi.app.services.produto;

import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.ProdutoAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.ProdutoNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.ProdutoMapper;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.ProdutoEntity;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.ProdutoRepository;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.SubcategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.MessageFormat;
import java.util.Optional;

import static com.github.andregpereira.resilientshop.productsapi.constants.ProdutoConstants.*;
import static com.github.andregpereira.resilientshop.productsapi.constants.ProdutoDtoConstants.*;
import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaConstants.SUBCATEGORIA;
import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaConstants.SUBCATEGORIA_ATUALIZADA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoManutencaoServiceTest {

    @InjectMocks
    private ProdutoManutencaoServiceImpl manutencaoService;

    @Mock
    private ProdutoMapper mapper;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private SubcategoriaRepository subcategoriaRepository;

    @Test
    void criarProdutoComDadosValidosRetornaProdutoDetalhesDto() {
        given(produtoRepository.existsBySku(PRODUTO_REGISTRO_DTO.sku())).willReturn(false);
        given(produtoRepository.existsByNome(PRODUTO_REGISTRO_DTO.nome())).willReturn(false);
        given(mapper.toProduto(PRODUTO_REGISTRO_DTO)).willReturn(PRODUTO_LOCAL_DATE_TIME_FIXADO);
        given(subcategoriaRepository.findById(1L)).willReturn(Optional.of(SUBCATEGORIA));
        given(produtoRepository.save(PRODUTO_LOCAL_DATE_TIME_FIXADO)).willReturn(PRODUTO_LOCAL_DATE_TIME_FIXADO);
        given(mapper.toProdutoDetalhesDto(PRODUTO_LOCAL_DATE_TIME_FIXADO)).willReturn(
                PRODUTO_DETALHES_DTO_LOCAL_DATE_TIME_FIXADO);
        assertThat(manutencaoService.criar(PRODUTO_REGISTRO_DTO)).isEqualTo(
                PRODUTO_DETALHES_DTO_LOCAL_DATE_TIME_FIXADO);
        then(produtoRepository).should().save(PRODUTO_LOCAL_DATE_TIME_FIXADO);
    }

    @Test
    void criarProdutoComDadosInvalidosThrowsException() {
        assertThatThrownBy(() -> manutencaoService.criar(PRODUTO_REGISTRO_DTO_INVALIDO)).isInstanceOf(
                RuntimeException.class);
        then(produtoRepository).should(never()).save(PRODUTO);
    }

    @Test
    void criarProdutoComSkuExistenteThrowsException() {
        given(produtoRepository.existsBySku(PRODUTO_REGISTRO_DTO.sku())).willReturn(true);
        assertThatThrownBy(() -> manutencaoService.criar(PRODUTO_REGISTRO_DTO)).isInstanceOf(
                ProdutoAlreadyExistsException.class).hasMessage(
                MessageFormat.format("Opa! Já existe um produto cadastrado com o SKU {0}",
                        PRODUTO_REGISTRO_DTO.sku().toString().replace(".", "")));
        then(produtoRepository).should(never()).save(PRODUTO);
    }

    @Test
    void criarProdutoComNomeExistenteThrowsException() {
        given(produtoRepository.existsBySku(PRODUTO_REGISTRO_DTO.sku())).willReturn(false);
        given(produtoRepository.existsByNome(PRODUTO_REGISTRO_DTO.nome())).willReturn(true);
        assertThatThrownBy(() -> manutencaoService.criar(PRODUTO_REGISTRO_DTO)).isInstanceOf(
                ProdutoAlreadyExistsException.class).hasMessage(
                MessageFormat.format("Opa! Já existe um produto cadastrado com o nome {0}",
                        PRODUTO_REGISTRO_DTO.nome()));
        then(produtoRepository).should(never()).save(PRODUTO);
    }

    @Test
    void criarProdutoComIdSubcategoriaInexistenteThrowsException() {
        given(produtoRepository.existsBySku(PRODUTO_REGISTRO_DTO.sku())).willReturn(false);
        given(produtoRepository.existsByNome(PRODUTO_REGISTRO_DTO.nome())).willReturn(false);
        given(subcategoriaRepository.findById(1L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> manutencaoService.criar(PRODUTO_REGISTRO_DTO)).isInstanceOf(
                SubcategoriaNotFoundException.class).hasMessage(
                MessageFormat.format("Ops! Nenhuma subcategoria foi encontrada com o id {0}",
                        PRODUTO_REGISTRO_DTO.idSubcategoria()));
        then(produtoRepository).should(never()).save(PRODUTO);
    }

    @Test
    void atualizarProdutoComDadosValidosRetornaProdutoDetalhesDto() {
        given(produtoRepository.findById(1L)).willReturn(Optional.of(PRODUTO));
        given(produtoRepository.existsByNome(PRODUTO_ATUALIZACAO_DTO.nome())).willReturn(false);
        given(subcategoriaRepository.findById(2L)).willReturn(Optional.of(SUBCATEGORIA_ATUALIZADA));
        given(produtoRepository.save(PRODUTO_ATUALIZADO)).willReturn(PRODUTO_ATUALIZADO);
        given(mapper.toProdutoDetalhesDto(PRODUTO_ATUALIZADO)).willReturn(PRODUTO_DETALHES_DTO_ATUALIZADO);
        assertThat(manutencaoService.atualizar(1L, PRODUTO_ATUALIZACAO_DTO)).isEqualTo(PRODUTO_DETALHES_DTO_ATUALIZADO);
        then(produtoRepository).should().save(PRODUTO_ATUALIZADO);
    }

    @Test
    void atualizarProdutoComDadosInvalidosThrowsException() {
        assertThatThrownBy(() -> manutencaoService.atualizar(10L, PRODUTO_ATUALIZACAO_DTO_INVALIDO)).isInstanceOf(
                RuntimeException.class);
        then(produtoRepository).should(never()).save(PRODUTO_ATUALIZADO);
    }

    @Test
    void atualizarProdutoComIdInexistenteThrowsException() {
        given(produtoRepository.findById(10L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> manutencaoService.atualizar(10L, PRODUTO_ATUALIZACAO_DTO)).isInstanceOf(
                ProdutoNotFoundException.class).hasMessage("Ops! Nenhum produto foi encontrado com o id 10");
        then(produtoRepository).should(never()).save(PRODUTO_ATUALIZADO);
    }

    @Test
    void atualizarProdutoComNomeExistenteThrowsException() {
        given(produtoRepository.findById(1L)).willReturn(Optional.of(PRODUTO));
        given(produtoRepository.existsByNome(PRODUTO_ATUALIZACAO_DTO.nome())).willReturn(true);
        assertThatThrownBy(() -> manutencaoService.atualizar(1L, PRODUTO_ATUALIZACAO_DTO)).isInstanceOf(
                ProdutoAlreadyExistsException.class).hasMessage(
                MessageFormat.format("Opa! Já existe um produto cadastrado com o nome {0}",
                        PRODUTO_ATUALIZACAO_DTO.nome()));
        then(produtoRepository).should(never()).save(PRODUTO_ATUALIZADO);
    }

    @Test
    void atualizarProdutoComIdSubcategoriaInexistenteThrowsException() {
        given(produtoRepository.findById(1L)).willReturn(Optional.of(PRODUTO));
        given(produtoRepository.existsByNome(PRODUTO_ATUALIZACAO_DTO.nome())).willReturn(false);
        given(subcategoriaRepository.findById(2L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> manutencaoService.atualizar(1L, PRODUTO_ATUALIZACAO_DTO)).isInstanceOf(
                SubcategoriaNotFoundException.class).hasMessage("Ops! Nenhuma subcategoria foi encontrada com o id 2");
        then(produtoRepository).should(never()).save(PRODUTO_ATUALIZADO);
    }

    @Test
    void removerProdutoComIdExistenteRetornaString() {
        given(produtoRepository.findById(1L)).willReturn(Optional.of(PRODUTO));
        assertThat(manutencaoService.desativar(1L)).isEqualTo("Produto com id 1 desativado com sucesso");
        then(produtoRepository).should().findById(1L);
        then(produtoRepository).should().save(PRODUTO);
    }

    @Test
    void removerProdutoComIdInexistenteThrowsException() {
        given(produtoRepository.findById(10L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> manutencaoService.desativar(10L)).isInstanceOf(
                ProdutoNotFoundException.class).hasMessage("Ops! Não foi encontrado um produto ativo com o id 10");
        then(produtoRepository).should(never()).save(any(ProdutoEntity.class));
    }

}
