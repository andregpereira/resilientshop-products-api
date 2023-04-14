package com.github.andregpereira.resilientshop.productsapi.services.produto;

import com.github.andregpereira.resilientshop.productsapi.app.dtos.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.produto.ProdutoDto;
import com.github.andregpereira.resilientshop.productsapi.app.services.produto.ProdutoConsultaServiceImpl;
import com.github.andregpereira.resilientshop.productsapi.entities.Produto;
import com.github.andregpereira.resilientshop.productsapi.infra.exceptions.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.infra.exceptions.ProdutoNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.infra.exceptions.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.mappers.ProdutoMapper;
import com.github.andregpereira.resilientshop.productsapi.repositories.CategoriaRepository;
import com.github.andregpereira.resilientshop.productsapi.repositories.ProdutoRepository;
import com.github.andregpereira.resilientshop.productsapi.repositories.SubcategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;

import static com.github.andregpereira.resilientshop.productsapi.constants.ProdutoConstants.PRODUTO;
import static com.github.andregpereira.resilientshop.productsapi.constants.ProdutoConstants.PRODUTO_ATUALIZADO;
import static com.github.andregpereira.resilientshop.productsapi.constants.ProdutoDtoConstants.*;
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

    @Spy
    private ProdutoMapper mapper = Mappers.getMapper(ProdutoMapper.class);

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
                ProdutoNotFoundException.class).hasMessage("Ops! Ainda não há produtos cadastrados");
    }

    @Test
    void consultarProdutoPorIdExistenteRetornaProdutoDetalhesDto() {
        given(produtoRepository.existsById(1L)).willReturn(true);
        when(produtoRepository.getReferenceById(1L)).thenReturn(PRODUTO);
        ProdutoDetalhesDto sut = produtoConsultaService.consultarPorId(1L);
        assertThat(sut).isNotNull().isEqualTo(PRODUTO_DETALHES_DTO);
    }

    @Test
    void consultarProdutoPorIdInexistenteThrowsException() {
        given(produtoRepository.existsById(10L)).willReturn(false);
        assertThatThrownBy(() -> produtoConsultaService.consultarPorId(10L)).isInstanceOf(
                ProdutoNotFoundException.class).hasMessage("Poxa! Nenhum produto foi encontrado com o id 10");
    }

    @Test
    void consultarProdutoPorNomeExistenteRetornaProdutoDto() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "nome");
        List<Produto> listaProdutos = new ArrayList<>();
        listaProdutos.add(PRODUTO);
        listaProdutos.add(PRODUTO_ATUALIZADO);
        Page<Produto> pageProdutos = new PageImpl<>(listaProdutos, pageable, 10);
        given(produtoRepository.findByNome("nome", pageable)).willReturn(pageProdutos);
        Page<ProdutoDto> sut = produtoConsultaService.consultarPorNome("nome", pageable);
        assertThat(sut).isNotEmpty().hasSize(2);
        assertThat(sut.getContent().get(0)).isEqualTo(PRODUTO_DTO);
        assertThat(sut.getContent().get(1)).isEqualTo(PRODUTO_DTO_ATUALIZADO);
    }

    @Test
    void consultarProdutoPorNomeInexistenteThrowsException() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "nome");
        given(produtoRepository.findByNome("produto", pageable)).willReturn(Page.empty());
        assertThatThrownBy(() -> produtoConsultaService.consultarPorNome("produto", pageable)).isInstanceOf(
                ProdutoNotFoundException.class).hasMessage(
                "Desculpe, não foi possível encontrar um produto com esse nome. Verifique e tente novamente");
    }

    @Test
    void consultarProdutoPorNomeEmBrancoThrowsException() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "nome");
        given(produtoRepository.findByNome(anyString(), any(Pageable.class))).willReturn(Page.empty());
        assertThatThrownBy(() -> produtoConsultaService.consultarPorNome("", pageable)).isInstanceOf(
                ProdutoNotFoundException.class).hasMessage(
                "Desculpe, não foi possível encontrar um produto com esse nome. Verifique e tente novamente");
    }

    @Test
    void consultarProdutoPorSubcategoriaExistenteRetornaProdutoDto() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        List<Produto> listaProdutos = new ArrayList<>();
        listaProdutos.add(PRODUTO);
        Page<Produto> pageProdutos = new PageImpl<>(listaProdutos, pageable, 10);
        given(subcategoriaRepository.existsById(1L)).willReturn(true);
        when(produtoRepository.findAllBySubcategoriaId(1L, pageable)).thenReturn(pageProdutos);
        Page<ProdutoDto> sut = produtoConsultaService.consultarPorSubcategoria(1L, pageable);
        assertThat(sut).isNotEmpty().hasSize(1);
        assertThat(sut.getContent().get(0)).isEqualTo(PRODUTO_DTO);
    }

    @Test
    void consultarProdutoPorSubcategoriaInexistenteThrowsException() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        given(subcategoriaRepository.existsById(10L)).willReturn(false);
        assertThatThrownBy(() -> produtoConsultaService.consultarPorSubcategoria(10L, pageable)).isInstanceOf(
                SubcategoriaNotFoundException.class).hasMessage(
                "Desculpe, não foi possível encontrar uma subcategoria com o id 10. Verifique e tente novamente");
    }

    @Test
    void consultarProdutoInexistentePorSubcategoriaExistenteThrowsException() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        given(subcategoriaRepository.existsById(1L)).willReturn(true);
        given(produtoRepository.findAllBySubcategoriaId(1L, pageable)).willReturn(Page.empty());
        assertThatThrownBy(() -> produtoConsultaService.consultarPorSubcategoria(1L, pageable)).isInstanceOf(
                ProdutoNotFoundException.class).hasMessage("Ops! Nenhum produto foi encontrado com essa subcategoria");
    }

    @Test
    void consultarProdutoPorCategoriaExistenteRetornaProdutoDto() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        List<Produto> listaProdutos = new ArrayList<>();
        listaProdutos.add(PRODUTO);
        Page<Produto> pageProdutos = new PageImpl<>(listaProdutos, pageable, 10);
        given(categoriaRepository.existsById(1L)).willReturn(true);
        when(produtoRepository.findAllBySubcategoriaCategoriaId(1L, pageable)).thenReturn(pageProdutos);
        Page<ProdutoDto> sut = produtoConsultaService.consultarPorCategoria(1L, pageable);
        assertThat(sut).isNotEmpty().hasSize(1);
        assertThat(sut.getContent().get(0)).isEqualTo(PRODUTO_DTO);
    }

    @Test
    void consultarProdutoPorCategoriaInexistenteThrowsException() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        given(categoriaRepository.existsById(10L)).willReturn(false);
        assertThatThrownBy(() -> produtoConsultaService.consultarPorCategoria(10L, pageable)).isInstanceOf(
                CategoriaNotFoundException.class).hasMessage(
                "Desculpe, não foi possível encontrar uma categoria com o id 10. Verifique e tente novamente");
    }

    @Test
    void consultarProdutoInexistentePorCategoriaExistenteThrowsException() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        given(categoriaRepository.existsById(1L)).willReturn(true);
        given(produtoRepository.findAllBySubcategoriaCategoriaId(1L, pageable)).willReturn(Page.empty());
        assertThatThrownBy(() -> produtoConsultaService.consultarPorCategoria(1L, pageable)).isInstanceOf(
                ProdutoNotFoundException.class).hasMessage("Ops! Nenhum produto foi encontrado com essa categoria");
    }

}

