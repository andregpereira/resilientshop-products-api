package com.github.andregpereira.resilientshop.productsapi.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.andregpereira.resilientshop.productsapi.app.config.ObjectMapperTestConfig;
import com.github.andregpereira.resilientshop.productsapi.app.dto.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.categoria.CategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.app.services.categoria.CategoriaConsultaService;
import com.github.andregpereira.resilientshop.productsapi.app.services.categoria.CategoriaManutencaoService;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.SubcategoriaNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;

import static com.github.andregpereira.resilientshop.productsapi.util.constant.CommonConstants.ROOT_JSON_PATH;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.CategoriaMockFactory.getCategoriaDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.CategoriaMockFactory.getCategoriaRegistroDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.CategoriaMockFactory.getCategoriaInvalida;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(ObjectMapperTestConfig.class)
@WebMvcTest(CategoriaController.class)
class CategoriaControllerTest {

    private static final String ROOT_URL = "/categorias";

    private static final String ID_URL = ROOT_URL.concat("/{id}");

    @MockBean
    private CategoriaManutencaoService manutencaoService;

    @MockBean
    private CategoriaConsultaService consultaService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static ResultMatcher dtoJsonPath() {
        return ROOT_JSON_PATH.value(getCategoriaDto());
    }

    private ResultMatcher pageJsonPath(Object o) {
        return ROOT_JSON_PATH.value(objectMapper.convertValue(o, JsonNode.class));
    }

    private String asString(Object dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    @Test
    void criarCategoriaComDadosValidosRetornaCreated() throws Exception {
        given(manutencaoService.criar(any(CategoriaRegistroDto.class))).willReturn(getCategoriaDto());
        mockMvc
            .perform(post(ROOT_URL)
                .content(asString(getCategoriaRegistroDto()))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(dtoJsonPath());
    }

    @Test
    void criarCategoriaComDadosInvalidosRetornaUnprocessableEntity() throws Exception {
        mockMvc
            .perform(post(ROOT_URL)
                .content(asString(getCategoriaInvalida()))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void criarSubcategoriaComNomeExistenteRetornaConflict() throws Exception {
        given(manutencaoService.criar(any(CategoriaRegistroDto.class))).willThrow(CategoriaAlreadyExistsException.class);
        mockMvc
            .perform(post(ROOT_URL)
                .content(asString(getCategoriaRegistroDto()))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isConflict());
    }

    @Test
    void atualizarCategoriaComDadosValidosRetornaOk() throws Exception {
        given(manutencaoService.atualizar(anyLong(), any(CategoriaRegistroDto.class))).willReturn(getCategoriaDto());
        mockMvc
            .perform(put(ID_URL, 1)
                .content(asString(getCategoriaRegistroDto()))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(dtoJsonPath());
    }

    @Test
    void atualizarCategoriaComDadosInvalidosRetornaUnprocessableEntity() throws Exception {
        mockMvc
            .perform(put(ID_URL, 1)
                .content(asString(getCategoriaInvalida()))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void atualizarCategoriaInexistenteRetornaNotFound() throws Exception {
        given(manutencaoService.atualizar(anyLong(), any(CategoriaRegistroDto.class)))
            .willThrow(CategoriaNotFoundException.class);
        mockMvc
            .perform(put(ID_URL, 1)
                .content(asString(getCategoriaRegistroDto()))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void removerCategoriaPorIdExistenteRetornaOk() throws Exception {
        final var msg = "Categoria removida";
        given(manutencaoService.remover(anyLong())).willReturn(msg);
        mockMvc
            .perform(delete(ID_URL, 10))
            .andExpect(status().isOk())
            .andExpect(ROOT_JSON_PATH.value(msg));
    }

    @Test
    void removerCategoriaPorIdInexistenteRetornaNotFound() throws Exception {
        given(manutencaoService.remover(anyLong())).willThrow(CategoriaNotFoundException.class);
        mockMvc
            .perform(delete(ID_URL, 10))
            .andExpect(status().isNotFound());
    }

    @Test
    void listarCategoriasExistentesRetornaOk() throws Exception {
        final var pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        final var listaCategorias = new ArrayList<CategoriaDto>();
        listaCategorias.add(getCategoriaDto());
        listaCategorias.add(getCategoriaDto());
        final var pageCategorias = new PageImpl<>(listaCategorias, pageable, 10);
        given(consultaService.listar(any(Pageable.class))).willReturn(pageCategorias);
        mockMvc
            .perform(get(ROOT_URL))
            .andExpect(status().isOk())
            .andExpect(pageJsonPath(pageCategorias));
    }

    @Test
    void listarCategoriasInexistentesRetornaNotFound() throws Exception {
        given(consultaService.listar(any(Pageable.class))).willThrow(CategoriaNotFoundException.class);
        mockMvc
            .perform(get(ROOT_URL))
            .andExpect(status().isNotFound());
    }

    @Test
    void consultarCategoriaPorIdExistenteRetornaOk() throws Exception {
        given(consultaService.consultarPorId(anyLong())).willReturn(getCategoriaDto());
        mockMvc
            .perform(get(ID_URL, 1))
            .andExpect(status().isOk())
            .andExpect(dtoJsonPath());
    }

    @Test
    void consultarCategoriaPorIdInexistenteRetornaNotFound() throws Exception {
        given(consultaService.consultarPorId(anyLong())).willThrow(SubcategoriaNotFoundException.class);
        mockMvc
            .perform(get(ID_URL, 10))
            .andExpect(status().isNotFound());
    }

    @Test
    void consultarCategoriaPorIdInvalidoRetornaBadRequest() throws Exception {
        final var msg = "Parâmetro inválido. Verifique e tente novamente";
        mockMvc
            .perform(get(ID_URL, "a"))
            .andExpect(status().isBadRequest())
            .andExpect(ROOT_JSON_PATH.value(msg));
    }

    @Test
    void criarCategoriaComRequestBodyNuloRetornaBadRequest() throws Exception {
        final var msg = "Informação inválida. Verifique os dados e tente novamente";
        mockMvc
            .perform(post(ROOT_URL)
                .content(asString(null))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(ROOT_JSON_PATH.value(msg));
    }

}
