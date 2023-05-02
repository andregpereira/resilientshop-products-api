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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaConstants.CATEGORIA;
import static com.github.andregpereira.resilientshop.productsapi.constants.ProdutoConstants.PRODUTO;
import static com.github.andregpereira.resilientshop.productsapi.constants.ProdutoConstants.PRODUTO_ATUALIZADO;
import static com.github.andregpereira.resilientshop.productsapi.constants.ProdutoDtoConstants.*;
import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaConstants.SUBCATEGORIA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoConsultaServiceTest {

    @InjectMocks
    private ProdutoConsultaServiceImpl produtoConsultaService;

    @Mock
    private ProdutoMapper mapper;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private SubcategoriaRepository subcategoriaRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Test
    void listarProdutosExistentesRetornaPageProdutoDto() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        List<Produto> listaProdutos = new ArrayList<>();
        listaProdutos.add(PRODUTO);
        listaProdutos.add(PRODUTO_ATUALIZADO);
        Page<Produto> pageProdutos = new PageImpl<>(listaProdutos, pageable, 10);
        given(produtoRepository.findAll(pageable)).willReturn(pageProdutos);
        given(mapper.toProdutoDto(PRODUTO)).willReturn(PRODUTO_DTO);
        given(mapper.toProdutoDto(PRODUTO_ATUALIZADO)).willReturn(PRODUTO_DTO_ATUALIZADO);
        Page<ProdutoDto> sut = produtoConsultaService.listar(pageable);
        assertThat(sut).isNotEmpty().hasSize(2);
        assertThat(sut.getContent().get(0)).isEqualTo(PRODUTO_DTO);
        assertThat(sut.getContent().get(1)).isEqualTo(PRODUTO_DTO_ATUALIZADO);
    }

    @Test
    void listarProdutosInexistentesThrowsException() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        given(produtoRepository.findAll(pageable)).willReturn(Page.empty());
        assertThatThrownBy(() -> produtoConsultaService.listar(pageable)).isInstanceOf(
                ProdutoNotFoundException.class).hasMessage("Poxa! Ainda não há produtos cadastrados");
    }

    @Test
    void consultarProdutoPorIdExistenteRetornaProdutoDetalhesDto() {
        given(produtoRepository.findById(1L)).willReturn(Optional.of(PRODUTO));
        given(mapper.toProdutoDetalhesDto(PRODUTO)).willReturn(PRODUTO_DETALHES_DTO);
        ProdutoDetalhesDto sut = produtoConsultaService.consultarPorId(1L);
        assertThat(sut).isNotNull().isEqualTo(PRODUTO_DETALHES_DTO);
    }

    @Test
    void consultarProdutoPorIdInexistenteThrowsException() {
        given(produtoRepository.findById(10L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> produtoConsultaService.consultarPorId(10L)).isInstanceOf(
                ProdutoNotFoundException.class).hasMessage("Ops! Nenhum produto foi encontrado com o id 10");
    }

    @Test
    void consultarProdutoPorNomeExistenteRetornaProdutoDto() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "nome");
        List<Produto> listaProdutos = new ArrayList<>();
        listaProdutos.add(PRODUTO);
        listaProdutos.add(PRODUTO_ATUALIZADO);
        Page<Produto> pageProdutos = new PageImpl<>(listaProdutos, pageable, 10);
        given(produtoRepository.findByNome("nome", pageable)).willReturn(pageProdutos);
        given(mapper.toProdutoDto(PRODUTO)).willReturn(PRODUTO_DTO);
        given(mapper.toProdutoDto(PRODUTO_ATUALIZADO)).willReturn(PRODUTO_DTO_ATUALIZADO);
        Page<ProdutoDto> sut = produtoConsultaService.consultarPorNome("nome", pageable);
        assertThat(sut).isNotEmpty().hasSize(2);
        assertThat(sut.getContent().get(0)).isEqualTo(PRODUTO_DTO);
        assertThat(sut.getContent().get(1)).isEqualTo(PRODUTO_DTO_ATUALIZADO);
    }

    @Test
    void consultarProdutoPorNomeInexistenteThrowsException() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "nome");
        given(produtoRepository.findByNome(anyString(), any(Pageable.class))).willReturn(Page.empty());
        assertThatThrownBy(() -> produtoConsultaService.consultarPorNome("produto", pageable)).isInstanceOf(
                ProdutoNotFoundException.class).hasMessage("Opa! Nenhum produto foi encontrado com o nome produto");
    }

    @Test
    void consultarProdutoPorNomeNuloThrowsException() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "nome");
        assertThatThrownBy(() -> produtoConsultaService.consultarPorNome(null, pageable)).isInstanceOf(
                RuntimeException.class);
    }

    @Test
    void consultarProdutoPorSubcategoriaExistenteRetornaProdutoDto() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        List<Produto> listaProdutos = new ArrayList<>();
        listaProdutos.add(PRODUTO);
        Page<Produto> pageProdutos = new PageImpl<>(listaProdutos, pageable, 10);
        given(subcategoriaRepository.findById(1L)).willReturn(Optional.of(SUBCATEGORIA));
        when(produtoRepository.findAllBySubcategoriaId(1L, pageable)).thenReturn(pageProdutos);
        given(mapper.toProdutoDto(PRODUTO)).willReturn(PRODUTO_DTO);
        Page<ProdutoDto> sut = produtoConsultaService.consultarPorSubcategoria(1L, pageable);
        assertThat(sut).isNotEmpty().hasSize(1);
        assertThat(sut.getContent().get(0)).isEqualTo(PRODUTO_DTO);
    }

    @Test
    void consultarProdutoPorSubcategoriaInexistenteThrowsException() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        given(subcategoriaRepository.findById(10L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> produtoConsultaService.consultarPorSubcategoria(10L, pageable)).isInstanceOf(
                SubcategoriaNotFoundException.class).hasMessage("Ops! Nenhuma subcategoria foi encontrada com o id 10");
    }

    @Test
    void consultarProdutoInexistentePorSubcategoriaExistenteThrowsException() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        given(subcategoriaRepository.findById(1L)).willReturn(Optional.of(SUBCATEGORIA));
        given(produtoRepository.findAllBySubcategoriaId(1L, pageable)).willReturn(Page.empty());
        assertThatThrownBy(() -> produtoConsultaService.consultarPorSubcategoria(1L, pageable)).isInstanceOf(
                ProdutoNotFoundException.class).hasMessage(
                MessageFormat.format("Opa! Nenhum produto foi encontrado com a subcategoria {0}",
                        SUBCATEGORIA.getNome()));
    }

    @Test
    void consultarProdutoPorCategoriaExistenteRetornaProdutoDto() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        List<Produto> listaProdutos = new ArrayList<>();
        listaProdutos.add(PRODUTO);
        Page<Produto> pageProdutos = new PageImpl<>(listaProdutos, pageable, 10);
        given(categoriaRepository.findById(1L)).willReturn(Optional.of(CATEGORIA));
        when(produtoRepository.findAllBySubcategoriaCategoriaId(1L, pageable)).thenReturn(pageProdutos);
        given(mapper.toProdutoDto(PRODUTO)).willReturn(PRODUTO_DTO);
        Page<ProdutoDto> sut = produtoConsultaService.consultarPorCategoria(1L, pageable);
        assertThat(sut).isNotEmpty().hasSize(1);
        assertThat(sut.getContent().get(0)).isEqualTo(PRODUTO_DTO);
    }

    @Test
    void consultarProdutoPorCategoriaInexistenteThrowsException() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        given(categoriaRepository.findById(10L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> produtoConsultaService.consultarPorCategoria(10L, pageable)).isInstanceOf(
                CategoriaNotFoundException.class).hasMessage("Ops! Nenhuma categoria foi encontrada com o id 10");
    }

    @Test
    void consultarProdutoInexistentePorCategoriaExistenteThrowsException() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        given(categoriaRepository.findById(1L)).willReturn(Optional.of(CATEGORIA));
        given(produtoRepository.findAllBySubcategoriaCategoriaId(1L, pageable)).willReturn(Page.empty());
        assertThatThrownBy(() -> produtoConsultaService.consultarPorCategoria(1L, pageable)).isInstanceOf(
                ProdutoNotFoundException.class).hasMessage(
                MessageFormat.format("Opa! Nenhum produto foi encontrado com a categoria {0}", CATEGORIA.getNome()));
    }

}

