package com.github.andregpereira.resilientshop.productsapi.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.andregpereira.resilientshop.productsapi.app.config.ObjectMapperTestConfig;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoAtualizacaoDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.app.services.produto.ProdutoConsultaService;
import com.github.andregpereira.resilientshop.productsapi.app.services.produto.ProdutoManutencaoService;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.ProdutoAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.ProdutoNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.SubcategoriaNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Set;

import static com.github.andregpereira.resilientshop.productsapi.util.constant.CommonConstants.ROOT_JSON_PATH;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.InvalidObjectMockFactory.readInvalidJson;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.ProdutoMockFactory.getPageIdProdutos;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.ProdutoMockFactory.getPageNomeProdutos;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.ProdutoMockFactory.getProdutoAtualizacaoDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.ProdutoMockFactory.getProdutoAtualizarEstoqueDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.ProdutoMockFactory.getProdutoDetalhesDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.ProdutoMockFactory.getProdutoRegistroDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(ObjectMapperTestConfig.class)
@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    private static final String URL_TEMPLATE = "/produtos";

    private static final String ID_PATH = "/{id}";

    private static final String URL_ID_PRODUTO = URL_TEMPLATE.concat(ID_PATH);

    private static final String URL_NOME_PRODUTO = URL_TEMPLATE.concat("/nome");

    private static final String URL_REATIVAR = URL_TEMPLATE
        .concat("/reativar")
        .concat(ID_PATH);

    private static final String URL_ESTOQUE = URL_TEMPLATE.concat("/estoque");

    private static final String URL_ESTOQUE_SUBTRAIR = URL_ESTOQUE.concat("/subtrair");

    private static final String URL_ESTOQUE_RETORNAR = URL_ESTOQUE.concat("/retornar");

    private static final String URL_ID_CATEGORIA = URL_TEMPLATE
        .concat("/categoria")
        .concat(ID_PATH);

    private static final String URL_ID_SUBCATEGORIA = URL_TEMPLATE
        .concat("/subcategoria")
        .concat(ID_PATH);

    @MockBean
    private ProdutoManutencaoService manutencaoService;

    @MockBean
    private ProdutoConsultaService consultaService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static ResultMatcher jsonPathRoot(Object o) {
        return ROOT_JSON_PATH.value(o);
    }

    private String asString(Object dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    private ResultMatcher jsonPathPageRoot(Object o) {
        return ROOT_JSON_PATH.value(objectMapper.convertValue(o, JsonNode.class));
    }

    @Test
    void criarProdutoComDadosValidosRetornaCreated() throws Exception {
        given(manutencaoService.criar(any(ProdutoRegistroDto.class))).willReturn(getProdutoDetalhesDto());
        mockMvc
            .perform(post(URL_TEMPLATE)
                .content(asString(getProdutoRegistroDto()))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPathRoot(getProdutoDetalhesDto()));
    }

    @Test
    void criarProdutoComDadosInvalidosRetornaUnprocessableEntity() throws Exception {
        mockMvc
            .perform(post(URL_TEMPLATE)
                .content(asString(readInvalidJson()))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void criarProdutoComSubcategoriaInexistenteRetornaNotFound() throws Exception {
        given(manutencaoService.criar(any(ProdutoRegistroDto.class))).willThrow(SubcategoriaNotFoundException.class);
        mockMvc
            .perform(post(URL_TEMPLATE)
                .content(asString(getProdutoRegistroDto()))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void criarProdutoComSkuOuNomeExistentesRetornaConflict() throws Exception {
        given(manutencaoService.criar(any(ProdutoRegistroDto.class))).willThrow(ProdutoAlreadyExistsException.class);
        mockMvc
            .perform(post(URL_TEMPLATE)
                .content(asString(getProdutoRegistroDto()))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isConflict());
    }

    @Test
    void atualizarProdutoComDadosValidosRetornaOkEProdutoDetalhesDto() throws Exception {
        given(manutencaoService.atualizar(
            anyLong(),
            any(ProdutoAtualizacaoDto.class)
        )).willReturn(getProdutoDetalhesDto());
        mockMvc
            .perform(put(URL_ID_PRODUTO, 1)
                .content(asString(getProdutoAtualizacaoDto()))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPathRoot(getProdutoDetalhesDto()));
    }

    @Test
    void atualizarProdutoComDadosInvalidosRetornaUnprocessableEntity() throws Exception {
        mockMvc
            .perform(put(URL_ID_PRODUTO, 1)
                .content(asString(readInvalidJson()))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void atualizarProdutoInexistenteRetornaNotFound() throws Exception {
        given(manutencaoService.atualizar(anyLong(), any(ProdutoAtualizacaoDto.class))).willThrow(
            ProdutoNotFoundException.class);
        mockMvc
            .perform(put(URL_ID_PRODUTO, 1)
                .content(asString(getProdutoAtualizacaoDto()))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void desativarProdutoPorIdExistenteRetornaOk() throws Exception {
        var mensagem = "Produto com id 10 desativado com sucesso";
        given(manutencaoService.desativar(anyLong())).willReturn(mensagem);
        mockMvc
            .perform(delete(URL_ID_PRODUTO, 10))
            .andExpect(status().isOk())
            .andExpect(ROOT_JSON_PATH.value(mensagem));
    }

    @Test
    void desativarProdutoPorIdInexistenteRetornaNotFound() throws Exception {
        given(manutencaoService.desativar(anyLong())).willThrow(ProdutoNotFoundException.class);
        mockMvc
            .perform(delete(URL_ID_PRODUTO, 1))
            .andExpect(status().isNotFound());
    }

    @Test
    void reativarProdutoPorIdExistenteRetornaOk() throws Exception {
        var mensagem = "Produto com id 10 reativado com sucesso";
        given(manutencaoService.reativar(anyLong())).willReturn(mensagem);
        mockMvc
            .perform(patch(URL_REATIVAR, 10))
            .andExpect(status().isOk())
            .andExpect(ROOT_JSON_PATH.value(mensagem));
    }

    @Test
    void reativarProdutoPorIdInexistenteRetornaNotFound() throws Exception {
        given(manutencaoService.reativar(anyLong())).willThrow(ProdutoNotFoundException.class);
        mockMvc
            .perform(patch(URL_REATIVAR, 1))
            .andExpect(status().isNotFound());
    }

    @Test
    void subtrairProdutosComIdsExistentesRetornaOk() throws Exception {
        willDoNothing()
            .given(manutencaoService)
            .subtrairEstoque(anySet());
        mockMvc
            .perform(put(URL_ESTOQUE_SUBTRAIR)
                .content(asString(Set.of(getProdutoAtualizarEstoqueDto())))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void subtrairProdutosComIdsInexistentesOuEstoquesInsuficientesRetornaNotFound() throws Exception {
        willThrow(ProdutoNotFoundException.class)
            .given(manutencaoService)
            .subtrairEstoque(anySet());
        mockMvc
            .perform(put(URL_ESTOQUE_SUBTRAIR)
                .content(asString(Set.of(getProdutoAtualizarEstoqueDto())))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void retornarProdutosComIdsExistentesRetornaOk() throws Exception {
        willDoNothing()
            .given(manutencaoService)
            .retornarEstoque(anySet());
        mockMvc
            .perform(put(URL_ESTOQUE_RETORNAR)
                .content(asString(Set.of(getProdutoAtualizarEstoqueDto())))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void retornarProdutosComIdsInexistentesRetornaNotFound() throws Exception {
        willThrow(ProdutoNotFoundException.class)
            .given(manutencaoService)
            .retornarEstoque(anySet());
        mockMvc
            .perform(put(URL_ESTOQUE_RETORNAR)
                .content(asString(Set.of(getProdutoAtualizarEstoqueDto())))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void listarProdutosExistentesRetornaOk() throws Exception {
        given(consultaService.listar(any(Pageable.class))).willReturn(getPageIdProdutos());
        mockMvc
            .perform(get(URL_TEMPLATE))
            .andExpect(status().isOk())
            .andExpect(jsonPathPageRoot(getPageIdProdutos()));
    }

    @Test
    void listarProdutosInexistentesRetornaNotFound() throws Exception {
        given(consultaService.listar(any(Pageable.class))).willThrow(ProdutoNotFoundException.class);
        mockMvc
            .perform(get(URL_TEMPLATE))
            .andExpect(status().isNotFound());
    }

    @Test
    void consultarProdutoPorIdExistenteRetornaProdutoDetalhesDto() throws Exception {
        given(consultaService.consultarPorId(anyLong())).willReturn(getProdutoDetalhesDto());
        mockMvc
            .perform(get(URL_ID_PRODUTO, 1))
            .andExpect(status().isOk())
            .andExpect(jsonPathRoot(getProdutoDetalhesDto()));
    }

    @Test
    void consultarProdutoPorIdInexistenteRetornaNotFound() throws Exception {
        given(consultaService.consultarPorId(anyLong())).willThrow(ProdutoNotFoundException.class);
        mockMvc
            .perform(get(URL_ID_PRODUTO, 10))
            .andExpect(status().isNotFound());
    }

    @Test
    void consultarProdutoPorIdInvalidoRetornaBadRequest() throws Exception {
        mockMvc
            .perform(get(URL_ID_PRODUTO, "a"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPathRoot("Parâmetro inválido. Verifique e tente novamente"));
    }

    @Test
    void consultarProdutoPorNomeExistenteRetornaProdutoDto() throws Exception {
        given(consultaService.consultarPorNome(anyString(), any(Pageable.class))).willReturn(getPageNomeProdutos());
        mockMvc
            .perform(get(URL_NOME_PRODUTO).queryParam("nome", "nome"))
            .andExpect(status().isOk())
            .andExpect(jsonPathPageRoot(getPageNomeProdutos()));
    }

    @Test
    void consultarProdutoPorNomeInexistenteRetornaNotFound() throws Exception {
        given(consultaService.consultarPorNome(
            anyString(),
            any(Pageable.class)
        )).willThrow(ProdutoNotFoundException.class);
        mockMvc
            .perform(get(URL_NOME_PRODUTO).queryParam("nome", "nomeProduto"))
            .andExpect(status().isNotFound());
    }

    @Test
    void consultarProdutoPorSubcategoriaExistenteRetornaProdutoDto() throws Exception {
        given(consultaService.consultarPorSubcategoria(
            anyLong(),
            any(Pageable.class)
        )).willReturn(getPageNomeProdutos());
        mockMvc
            .perform(get(URL_ID_SUBCATEGORIA, 1))
            .andExpect(status().isOk())
            .andExpect(jsonPathPageRoot(getPageNomeProdutos()));
    }

    @Test
    void consultarProdutoPorSubcategoriaInexistenteRetornaNotFound() throws Exception {
        given(consultaService.consultarPorSubcategoria(anyLong(), any(Pageable.class))).willThrow(
            ProdutoNotFoundException.class);
        mockMvc
            .perform(get(URL_ID_SUBCATEGORIA, 20))
            .andExpect(status().isNotFound());
    }

    @Test
    void consultarProdutoPorCategoriaExistenteRetornaProdutoDto() throws Exception {
        given(consultaService.consultarPorCategoria(anyLong(), any(Pageable.class))).willReturn(getPageNomeProdutos());
        mockMvc
            .perform(get(URL_ID_CATEGORIA, 5))
            .andExpect(status().isOk())
            .andExpect(jsonPathPageRoot(getPageNomeProdutos()));
    }

    @Test
    void consultarProdutoPorCategoriaInexistenteRetornaNotFound() throws Exception {
        given(consultaService.consultarPorCategoria(
            anyLong(),
            any(Pageable.class)
        )).willThrow(ProdutoNotFoundException.class);
        mockMvc
            .perform(get(URL_ID_CATEGORIA, 25))
            .andExpect(status().isNotFound());
    }

    @Test
    void criarProdutoComRequestBodyNuloRetornaBadRequest() throws Exception {
        mockMvc
            .perform(post(URL_TEMPLATE)
                .content(asString(null))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPathRoot("Informação inválida. Verifique os dados e tente novamente"));
    }

    @Test
    void inserirParametroNomeVazioThrowsException() throws Exception {
        mockMvc
            .perform(get(URL_NOME_PRODUTO).queryParam("nome", ""))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$[0].campo").value("nome"),
                jsonPath("$[0].mensagem").value("O nome deve ter pelo menos 2 caracteres")
            );
    }

    @Test
    void inserirParametroNomeNuloThrowsException() throws Exception {
        mockMvc
            .perform(get(URL_NOME_PRODUTO))
            .andExpect(status().isBadRequest())
            .andExpectAll(
                jsonPath("$.campo").value("nome"),
                jsonPath("$.mensagem").value("O campo nome é obrigatório")
            );
    }

}
