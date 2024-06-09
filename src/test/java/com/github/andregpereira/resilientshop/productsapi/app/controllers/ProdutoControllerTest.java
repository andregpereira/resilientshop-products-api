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
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;


import static com.github.andregpereira.resilientshop.productsapi.constants.ProdutoDtoConstants.PAGE_ID_PRODUTOS;
import static com.github.andregpereira.resilientshop.productsapi.constants.ProdutoDtoConstants.PAGE_NOME_PRODUTOS;
import static com.github.andregpereira.resilientshop.productsapi.constants.ProdutoDtoConstants.PRODUTO_ATUALIZACAO_DTO;
import static com.github.andregpereira.resilientshop.productsapi.constants.ProdutoDtoConstants.PRODUTO_ATUALIZACAO_DTO_INVALIDO;
import static com.github.andregpereira.resilientshop.productsapi.constants.ProdutoDtoConstants.PRODUTO_DETALHES_DTO;
import static com.github.andregpereira.resilientshop.productsapi.constants.ProdutoDtoConstants.PRODUTO_DETALHES_DTO_ATUALIZADO;
import static com.github.andregpereira.resilientshop.productsapi.constants.ProdutoDtoConstants.PRODUTO_REGISTRO_DTO;
import static com.github.andregpereira.resilientshop.productsapi.constants.ProdutoDtoConstants.PRODUTO_REGISTRO_DTO_INVALIDO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(ObjectMapperTestConfig.class)
@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    private static final String URL_TEMPLATE = "/produtos";

    private static final String PATH_ID = "/{id}";

    private static final String URL_ID_PRODUTO = URL_TEMPLATE.concat(PATH_ID);

    private static final String URL_NOME_PRODUTO = URL_TEMPLATE.concat("/nome");

    private static final String URL_ID_CATEGORIA = URL_TEMPLATE.concat("/categoria").concat(PATH_ID);

    private static final String URL_ID_SUBCATEGORIA = URL_TEMPLATE.concat("/subcategoria").concat(PATH_ID);

    private static final JsonPathResultMatchers JSON_PATH_ROOT = jsonPath("$");

    @MockBean
    private ProdutoManutencaoService manutencaoService;

    @MockBean
    private ProdutoConsultaService consultaService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static ResultMatcher jsonPathRoot(Object o) {
        return JSON_PATH_ROOT.value(o);
    }

    private String getValueAsString(Record dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    private ResultMatcher jsonPathPageRoot(Object o) {
        return JSON_PATH_ROOT.value(objectMapper.convertValue(o, JsonNode.class));
    }

    @Test
    void criarProdutoComDadosValidosRetornaCreated() throws Exception {
        given(manutencaoService.criar(any(ProdutoRegistroDto.class))).willReturn(PRODUTO_DETALHES_DTO);
        mockMvc.perform(post(URL_TEMPLATE).content(getValueAsString(PRODUTO_REGISTRO_DTO))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPathRoot(PRODUTO_DETALHES_DTO));
    }

    @Test
    void criarProdutoComDadosInvalidosRetornaUnprocessableEntity() throws Exception {
        mockMvc.perform(post(URL_TEMPLATE).content(getValueAsString(PRODUTO_REGISTRO_DTO_INVALIDO))
                .contentType(APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
    }

    @Test
    void criarProdutoComSubcategoriaInexistenteRetornaNotFound() throws Exception {
        given(manutencaoService.criar(any(ProdutoRegistroDto.class))).willThrow(SubcategoriaNotFoundException.class);
        mockMvc.perform(post(URL_TEMPLATE).content(getValueAsString(PRODUTO_REGISTRO_DTO))
                .contentType(APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void criarProdutoComSkuOuNomeExistentesRetornaConflict() throws Exception {
        given(manutencaoService.criar(any(ProdutoRegistroDto.class))).willThrow(ProdutoAlreadyExistsException.class);
        mockMvc.perform(post(URL_TEMPLATE).content(getValueAsString(PRODUTO_REGISTRO_DTO))
                .contentType(APPLICATION_JSON)).andExpect(status().isConflict());
    }

    @Test
    void atualizarProdutoComDadosValidosRetornaOkEProdutoDetalhesDto() throws Exception {
        given(manutencaoService.atualizar(anyLong(), any(ProdutoAtualizacaoDto.class))).willReturn(
                PRODUTO_DETALHES_DTO_ATUALIZADO);
        mockMvc.perform(put(URL_ID_PRODUTO, 1).content(getValueAsString(PRODUTO_ATUALIZACAO_DTO))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPathRoot(PRODUTO_DETALHES_DTO_ATUALIZADO));
    }

    @Test
    void atualizarProdutoComDadosInvalidosRetornaUnprocessableEntity() throws Exception {
        mockMvc.perform(put(URL_ID_PRODUTO, 1).content(getValueAsString(PRODUTO_ATUALIZACAO_DTO_INVALIDO))
                .contentType(APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
    }

    @Test
    void atualizarProdutoInexistenteRetornaNotFound() throws Exception {
        given(manutencaoService.atualizar(anyLong(), any(ProdutoAtualizacaoDto.class))).willThrow(
                ProdutoNotFoundException.class);
        mockMvc.perform(put(URL_ID_PRODUTO, 1).content(getValueAsString(PRODUTO_ATUALIZACAO_DTO))
                .contentType(APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void removerProdutoPorIdExistenteRetornaOk() throws Exception {
        var mensagem = "Produto com id 10 removido com sucesso";
        given(manutencaoService.desativar(anyLong())).willReturn(mensagem);
        mockMvc.perform(delete(URL_ID_PRODUTO, 10))
                .andExpect(status().isOk())
                .andExpect(JSON_PATH_ROOT.value(mensagem));
    }

    @Test
    void removerProdutoPorIdInexistenteRetornaNotFound() throws Exception {
        given(manutencaoService.desativar(anyLong())).willThrow(ProdutoNotFoundException.class);
        mockMvc.perform(delete(URL_ID_PRODUTO, 1)).andExpect(status().isNotFound());
    }

    @Test
    void listarProdutosExistentesRetornaOk() throws Exception {
        given(consultaService.listar(any(Pageable.class))).willReturn(PAGE_ID_PRODUTOS);
        mockMvc.perform(get(URL_TEMPLATE)).andExpect(status().isOk()).andExpect(jsonPathPageRoot(PAGE_ID_PRODUTOS));
    }

    @Test
    void listarProdutosInexistentesRetornaNotFound() throws Exception {
        given(consultaService.listar(any(Pageable.class))).willThrow(ProdutoNotFoundException.class);
        mockMvc.perform(get(URL_TEMPLATE)).andExpect(status().isNotFound());
    }

    @Test
    void consultarProdutoPorIdExistenteRetornaProdutoDetalhesDto() throws Exception {
        given(consultaService.consultarPorId(anyLong())).willReturn(PRODUTO_DETALHES_DTO);
        mockMvc.perform(get(URL_ID_PRODUTO, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPathRoot(PRODUTO_DETALHES_DTO));
    }

    @Test
    void consultarProdutoPorIdInexistenteRetornaNotFound() throws Exception {
        given(consultaService.consultarPorId(anyLong())).willThrow(ProdutoNotFoundException.class);
        mockMvc.perform(get(URL_ID_PRODUTO, 10)).andExpect(status().isNotFound());
    }

    @Test
    void consultarProdutoPorIdInvalidoRetornaBadRequest() throws Exception {
        mockMvc.perform(get(URL_ID_PRODUTO, "a"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPathRoot("Parâmetro inválido. Verifique e tente novamente"));
    }

    @Test
    void consultarProdutoPorNomeExistenteRetornaProdutoDto() throws Exception {
        given(consultaService.consultarPorNome(anyString(), any(Pageable.class))).willReturn(PAGE_NOME_PRODUTOS);
        mockMvc.perform(get(URL_NOME_PRODUTO).queryParam("nome", "nome"))
                .andExpect(status().isOk())
                .andExpect(jsonPathPageRoot(PAGE_NOME_PRODUTOS));
    }

    @Test
    void consultarProdutoPorNomeInexistenteRetornaNotFound() throws Exception {
        given(consultaService.consultarPorNome(anyString(),
                any(Pageable.class))).willThrow(ProdutoNotFoundException.class);
        mockMvc.perform(get(URL_NOME_PRODUTO).queryParam("nome", "nomeProduto")).andExpect(status().isNotFound());
    }

    @Test
    void consultarProdutoPorSubcategoriaExistenteRetornaProdutoDto() throws Exception {
        given(consultaService.consultarPorSubcategoria(anyLong(), any(Pageable.class))).willReturn(PAGE_NOME_PRODUTOS);
        mockMvc.perform(get(URL_ID_SUBCATEGORIA, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPathPageRoot(PAGE_NOME_PRODUTOS));
    }

    @Test
    void consultarProdutoPorSubcategoriaInexistenteRetornaNotFound() throws Exception {
        given(consultaService.consultarPorSubcategoria(anyLong(), any(Pageable.class))).willThrow(
                ProdutoNotFoundException.class);
        mockMvc.perform(get(URL_ID_SUBCATEGORIA, 20)).andExpect(status().isNotFound());
    }

    @Test
    void consultarProdutoPorCategoriaExistenteRetornaProdutoDto() throws Exception {
        given(consultaService.consultarPorCategoria(anyLong(), any(Pageable.class))).willReturn(PAGE_NOME_PRODUTOS);
        mockMvc.perform(get(URL_ID_CATEGORIA, 5))
                .andExpect(status().isOk())
                .andExpect(jsonPathPageRoot(PAGE_NOME_PRODUTOS));
    }

    @Test
    void consultarProdutoPorCategoriaInexistenteRetornaNotFound() throws Exception {
        given(consultaService.consultarPorCategoria(anyLong(),
                any(Pageable.class))).willThrow(ProdutoNotFoundException.class);
        mockMvc.perform(get(URL_ID_CATEGORIA, 25)).andExpect(status().isNotFound());
    }

    @Test
    void criarProdutoComRequestBodyNuloRetornaBadRequest() throws Exception {
        mockMvc.perform(post(URL_TEMPLATE).content(getValueAsString(null)).contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPathRoot("Informação inválida. Verifique os dados e tente novamente"));
    }

    @Test
    void inserirParametroNomeVazioThrowsException() throws Exception {
        mockMvc.perform(get(URL_NOME_PRODUTO).queryParam("nome", ""))
                .andExpect(status().isBadRequest())
                .andExpectAll(jsonPath("$[0].campo").value("nome"),
                        jsonPath("$[0].mensagem").value("O nome deve ter pelo menos 2 caracteres"));
    }

    @Test
    void inserirParametroNomeNuloThrowsException() throws Exception {
        mockMvc.perform(get(URL_NOME_PRODUTO))
                .andExpect(status().isBadRequest())
                .andExpectAll(jsonPath("$.campo").value("nome"),
                        jsonPath("$.mensagem").value("O campo nome é obrigatório"));
    }

}
