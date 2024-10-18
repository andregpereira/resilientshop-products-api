package com.github.andregpereira.resilientshop.productsapi.app.services.produto;

import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoAtualizacaoDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.ProdutoAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.ProdutoNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.ProdutoMapper;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.ProdutoEntity;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.CategoriaRepository;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.ProdutoRepository;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.SubcategoriaRepository;
import org.assertj.core.api.BDDAssertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.Set;

import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.CategoriaMockFactory.getCategoriaEntity;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.ProdutoMockFactory.getProdutoAtualizacaoDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.ProdutoMockFactory.getProdutoAtualizarEstoqueDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.ProdutoMockFactory.getProdutoDetalhesDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.ProdutoMockFactory.getProdutoEntity;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.ProdutoMockFactory.getProdutoRegistroDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.ProdutoMockFactory.jsonInvalido;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.SubcategoriaMockFactory.getSubcategoriaEntity;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.then;

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

    @Mock
    private CategoriaRepository categoriaRepository;

    @Test
    void criarProdutoComDadosValidosRetornaProdutoDetalhesDto() {
        given(produtoRepository.existsBySku(anyLong())).willReturn(false);
        given(produtoRepository.existsByNome(anyString())).willReturn(false);
        given(mapper.toProduto(any(ProdutoRegistroDto.class))).willReturn(getProdutoEntity());
        given(categoriaRepository.findById(anyLong())).willReturn(Optional.of(getCategoriaEntity()));
        given(subcategoriaRepository.findById(anyLong())).willReturn(Optional.of(getSubcategoriaEntity()));
        given(produtoRepository.save(any(ProdutoEntity.class))).willReturn(getProdutoEntity());
        given(mapper.toProdutoDetalhesDto(any(ProdutoEntity.class))).willReturn(getProdutoDetalhesDto());

        final var sut = manutencaoService.criar(getProdutoRegistroDto());

        BDDAssertions
            .then(sut)
            .isEqualTo(getProdutoDetalhesDto());
        then(produtoRepository)
            .should()
            .save(any(ProdutoEntity.class));
    }

    @Test
    void criarProdutoComDadosValidosSemSubcategoriaRetornaProdutoDetalhesDto() {
        final var mock = getProdutoRegistroDto();
        final var dto = new ProdutoRegistroDto(
            mock.sku(),
            mock.nome(),
            mock.descricao(),
            mock.valorUnitario(),
            mock.estoque(),
            mock.imageUrl(),
            mock.categoriaId(),
            null
        );
        given(produtoRepository.existsBySku(anyLong())).willReturn(false);
        given(produtoRepository.existsByNome(anyString())).willReturn(false);
        given(mapper.toProduto(any(ProdutoRegistroDto.class))).willReturn(getProdutoEntity());
        given(categoriaRepository.findById(anyLong())).willReturn(Optional.of(getCategoriaEntity()));
        given(produtoRepository.save(any(ProdutoEntity.class))).willReturn(getProdutoEntity());
        given(mapper.toProdutoDetalhesDto(any(ProdutoEntity.class))).willReturn(getProdutoDetalhesDto());

        final var sut = manutencaoService.criar(dto);

        BDDAssertions
            .then(sut)
            .isEqualTo(getProdutoDetalhesDto());
        then(produtoRepository)
            .should()
            .existsBySku(anyLong());
        then(produtoRepository)
            .should()
            .existsByNome(anyString());
        then(mapper)
            .should()
            .toProduto(any(ProdutoRegistroDto.class));
        then(categoriaRepository)
            .should()
            .findById(anyLong());
        then(produtoRepository)
            .should()
            .save(any(ProdutoEntity.class));
        then(mapper)
            .should()
            .toProdutoDetalhesDto(any(ProdutoEntity.class));
        then(subcategoriaRepository).shouldHaveNoInteractions();
    }

    @Test
    void criarProdutoComDadosInvalidosThrowsException() {
        final ThrowingCallable sut = () -> manutencaoService.criar((ProdutoRegistroDto) jsonInvalido());

        assertThatThrownBy(sut).isInstanceOf(RuntimeException.class);
        then(produtoRepository)
            .should(never())
            .save(any(ProdutoEntity.class));
    }

    @Test
    void criarProdutoComSkuExistenteThrowsException() {
        final var dto = getProdutoRegistroDto();
        given(produtoRepository.existsBySku(anyLong())).willReturn(true);

        final ThrowingCallable sut = () -> manutencaoService.criar(dto);

        assertThatThrownBy(sut)
            .isInstanceOf(ProdutoAlreadyExistsException.class)
            .hasMessage(MessageFormat.format(
                "Opa! Já existe um produto cadastrado com o SKU {0}",
                dto
                    .sku()
                    .toString()
                    .replace(".", "")
            ));
        then(produtoRepository)
            .should(never())
            .save(any(ProdutoEntity.class));
    }

    @Test
    void criarProdutoComNomeExistenteThrowsException() {
        final var dto = getProdutoRegistroDto();
        given(produtoRepository.existsBySku(anyLong())).willReturn(false);
        given(produtoRepository.existsByNome(anyString())).willReturn(true);

        final ThrowingCallable sut = () -> manutencaoService.criar(dto);

        assertThatThrownBy(sut)
            .isInstanceOf(ProdutoAlreadyExistsException.class)
            .hasMessage(MessageFormat.format("Opa! Já existe um produto cadastrado com o nome {0}", dto.nome()));
        then(produtoRepository)
            .should(never())
            .save(any(ProdutoEntity.class));
    }

    @Test
    void criarProdutoComIdCategoriaInexistenteThrowsException() {
        final var dto = getProdutoRegistroDto();
        given(produtoRepository.existsBySku(anyLong())).willReturn(false);
        given(produtoRepository.existsByNome(anyString())).willReturn(false);
        given(categoriaRepository.findById(anyLong())).willReturn(Optional.empty());

        final ThrowingCallable sut = () -> manutencaoService.criar(dto);

        assertThatThrownBy(sut)
            .isInstanceOf(CategoriaNotFoundException.class)
            .hasMessage(MessageFormat.format(
                "Ops! Nenhuma categoria foi encontrada com o id {0}",
                dto.subcategoriaId()
            ));
        then(produtoRepository)
            .should(never())
            .save(any(ProdutoEntity.class));
    }

    @Test
    void atualizarProdutoComDadosValidosRetornaProdutoDetalhesDto() {
        given(produtoRepository.findById(anyLong())).willReturn(Optional.of(getProdutoEntity()));
        given(produtoRepository.existsByNome(anyString())).willReturn(false);
        given(categoriaRepository.findById(anyLong())).willReturn(Optional.of(getCategoriaEntity()));
        given(subcategoriaRepository.findById(anyLong())).willReturn(Optional.of(getSubcategoriaEntity()));
        given(produtoRepository.save(any(ProdutoEntity.class))).willReturn(getProdutoEntity());
        given(mapper.toProdutoDetalhesDto(any(ProdutoEntity.class))).willReturn(getProdutoDetalhesDto());

        final var sut = manutencaoService.atualizar(1L, getProdutoAtualizacaoDto());

        BDDAssertions
            .then(sut)
            .isEqualTo(getProdutoDetalhesDto());
        then(produtoRepository)
            .should()
            .save(any(ProdutoEntity.class));
    }

    @Test
    void atualizarProdutoComDadosValidosSemSubcategoriaRetornaProdutoDetalhesDto() {
        final var mock = getProdutoAtualizacaoDto();
        final var dto = new ProdutoAtualizacaoDto(
            mock.nome(),
            mock.descricao(),
            mock.valorUnitario(),
            mock.estoque(),
            mock.imageUrl(),
            mock.ativo(),
            mock.categoriaId(),
            null
        );
        given(produtoRepository.findById(anyLong())).willReturn(Optional.of(getProdutoEntity()));
        given(produtoRepository.existsByNome(anyString())).willReturn(false);
        given(categoriaRepository.findById(anyLong())).willReturn(Optional.of(getCategoriaEntity()));
        given(produtoRepository.save(any(ProdutoEntity.class))).willReturn(getProdutoEntity());
        given(mapper.toProdutoDetalhesDto(any(ProdutoEntity.class))).willReturn(getProdutoDetalhesDto());

        final var sut = manutencaoService.atualizar(1L, dto);

        BDDAssertions
            .then(sut)
            .isEqualTo(getProdutoDetalhesDto());
        then(produtoRepository)
            .should()
            .findById(anyLong());
        then(produtoRepository)
            .should()
            .existsByNome(anyString());
        then(categoriaRepository)
            .should()
            .findById(anyLong());
        then(produtoRepository)
            .should()
            .save(any(ProdutoEntity.class));
        then(mapper)
            .should()
            .toProdutoDetalhesDto(any(ProdutoEntity.class));
        then(subcategoriaRepository).shouldHaveNoInteractions();
    }

    @Test
    void atualizarProdutoComDadosInvalidosThrowsException() {
        final ThrowingCallable sut = () -> manutencaoService.atualizar(10L, (ProdutoAtualizacaoDto) jsonInvalido());

        assertThatThrownBy(sut).isInstanceOf(RuntimeException.class);
        then(produtoRepository)
            .should(never())
            .save(any(ProdutoEntity.class));
    }

    @Test
    void atualizarProdutoComIdInexistenteThrowsException() {
        given(produtoRepository.findById(anyLong())).willReturn(Optional.empty());

        final ThrowingCallable sut = () -> manutencaoService.atualizar(10L, getProdutoAtualizacaoDto());

        assertThatThrownBy(sut)
            .isInstanceOf(ProdutoNotFoundException.class)
            .hasMessage("Ops! Nenhum produto foi encontrado com o id 10");
        then(produtoRepository)
            .should(never())
            .save(any(ProdutoEntity.class));
    }

    @Test
    void atualizarProdutoComNomeExistenteThrowsException() {
        final var produto = getProdutoEntity();
        given(produtoRepository.findById(anyLong())).willReturn(Optional.of(produto));
        given(produtoRepository.existsByNome(anyString())).willReturn(true);

        final ThrowingCallable sut = () -> manutencaoService.atualizar(1L, getProdutoAtualizacaoDto());

        assertThatThrownBy(sut)
            .isInstanceOf(ProdutoAlreadyExistsException.class)
            .hasMessage(MessageFormat.format("Opa! Já existe um produto cadastrado com o nome {0}", produto.getNome()));
        then(produtoRepository)
            .should(never())
            .save(any(ProdutoEntity.class));
    }

    @Test
    void atualizarProdutoComIdCategoriaInexistenteThrowsException() {
        given(produtoRepository.findById(anyLong())).willReturn(Optional.of(getProdutoEntity()));
        given(produtoRepository.existsByNome(anyString())).willReturn(false);
        given(categoriaRepository.findById(anyLong())).willReturn(Optional.empty());

        final ThrowingCallable sut = () -> manutencaoService.atualizar(1L, getProdutoAtualizacaoDto());

        assertThatThrownBy(sut)
            .isInstanceOf(CategoriaNotFoundException.class)
            .hasMessage("Ops! Nenhuma categoria foi encontrada com o id 1");
        then(produtoRepository)
            .should(never())
            .save(any(ProdutoEntity.class));
    }

    @Test
    void desativarProdutoComIdExistenteRetornaString() {
        given(produtoRepository.findById(anyLong())).willReturn(Optional.of(getProdutoEntity()));

        final var sut = manutencaoService.desativar(1L);

        BDDAssertions
            .then(sut)
            .isEqualTo("Produto com id 1 desativado com sucesso");
        then(produtoRepository)
            .should()
            .findById(anyLong());
        then(produtoRepository)
            .should()
            .save(any(ProdutoEntity.class));
    }

    @Test
    void desativarProdutoComIdInexistenteThrowsException() {
        given(produtoRepository.findById(anyLong())).willReturn(Optional.empty());

        final ThrowingCallable sut = () -> manutencaoService.desativar(10L);

        assertThatThrownBy(sut)
            .isInstanceOf(ProdutoNotFoundException.class)
            .hasMessage("Ops! Não foi encontrado um produto ativo com o id 10");
        then(produtoRepository)
            .should(never())
            .save(any(ProdutoEntity.class));
    }

    @Test
    void reativarProdutoComIdExistenteRetornaString() {
        given(produtoRepository.findById(anyLong())).willReturn(Optional.of(getProdutoEntity()));

        final var sut = manutencaoService.reativar(1L);

        BDDAssertions
            .then(sut)
            .isEqualTo("Produto com id 1 reativado com sucesso");
        then(produtoRepository)
            .should()
            .findById(anyLong());
        then(produtoRepository)
            .should()
            .save(any(ProdutoEntity.class));
    }

    @Test
    void reativarProdutoComIdInexistenteThrowsException() {
        given(produtoRepository.findById(anyLong())).willReturn(Optional.empty());

        final ThrowingCallable sut = () -> manutencaoService.reativar(10L);

        assertThatThrownBy(sut)
            .isInstanceOf(ProdutoNotFoundException.class)
            .hasMessage("Ops! Não foi encontrado um produto inativo com o id 10");
        then(produtoRepository)
            .should(never())
            .save(any(ProdutoEntity.class));
    }

    @Test
    void subtrairProdutosComIdsExistentes() {
        given(produtoRepository.findById(anyLong())).willReturn(Optional.of(getProdutoEntity()));

        manutencaoService.subtrairEstoque(Set.of(getProdutoAtualizarEstoqueDto()));

        then(produtoRepository)
            .should()
            .findById(anyLong());
        then(produtoRepository)
            .should()
            .save(any(ProdutoEntity.class));
    }

    @Test
    void subtrairProdutosComIdsInexistentesThrowsException() {
        given(produtoRepository.findById(anyLong())).willReturn(Optional.empty());

        final ThrowingCallable sut = () -> manutencaoService.subtrairEstoque(Set.of(getProdutoAtualizarEstoqueDto()));

        assertThatThrownBy(sut)
            .isInstanceOf(ProdutoNotFoundException.class)
            .hasMessage("Ops! Nenhum produto foi encontrado com o id 1");
        then(produtoRepository)
            .should(never())
            .save(any(ProdutoEntity.class));
    }

    @Test
    void subtrairProdutosComEstoqueInsuficienteThrowsException() {
        final var produto = getProdutoEntity();
        produto.setEstoque(0);
        given(produtoRepository.findById(anyLong())).willReturn(Optional.of(produto));

        final ThrowingCallable sut = () -> manutencaoService.subtrairEstoque(Set.of(getProdutoAtualizarEstoqueDto()));

        assertThatThrownBy(sut)
            .isInstanceOf(ProdutoNotFoundException.class)
            .hasMessage(MessageFormat.format("Ops! O produto {0} não possui estoque suficiente", produto.getNome()));
        then(produtoRepository)
            .should(never())
            .save(any(ProdutoEntity.class));
    }

    @Test
    void retornarProdutosComIdsExistentes() {
        given(produtoRepository.findById(anyLong())).willReturn(Optional.of(getProdutoEntity()));

        manutencaoService.retornarEstoque(Set.of(getProdutoAtualizarEstoqueDto()));

        then(produtoRepository)
            .should()
            .findById(anyLong());
        then(produtoRepository)
            .should()
            .save(any(ProdutoEntity.class));
    }

    @Test
    void retornarProdutosComIdsInexistentesThrowsException() {
        given(produtoRepository.findById(anyLong())).willReturn(Optional.empty());

        final ThrowingCallable sut = () -> manutencaoService.retornarEstoque(Set.of(getProdutoAtualizarEstoqueDto()));

        assertThatThrownBy(sut)
            .isInstanceOf(ProdutoNotFoundException.class)
            .hasMessage("Ops! Nenhum produto foi encontrado com o id 1");
        then(produtoRepository)
            .should(never())
            .save(any(ProdutoEntity.class));
    }

}
