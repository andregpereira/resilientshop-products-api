package com.github.andregpereira.resilientshop.productsapi.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.andregpereira.resilientshop.productsapi.app.config.ObjectMapperTestConfig;
import com.github.andregpereira.resilientshop.productsapi.app.dto.subcategoria.SubcategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.subcategoria.SubcategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.app.services.subcategoria.SubcategoriaConsultaService;
import com.github.andregpereira.resilientshop.productsapi.app.services.subcategoria.SubcategoriaManutencaoService;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.SubcategoriaAlreadyExistsException;
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
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.SubcategoriaMockFactory.getSubcategoriaDetalhesDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.SubcategoriaMockFactory.getSubcategoriaDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.SubcategoriaMockFactory.getSubcategoriaRegistroDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.SubcategoriaMockFactory.getSubcategoriaInvalida;
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
@WebMvcTest(SubcategoriaController.class)
class SubcategoriaControllerTest {

    private static final String ROOT_URL = "/subcategorias";

    private static final String ID_URL = ROOT_URL.concat("/{id}");

    @MockBean
    private SubcategoriaManutencaoService manutencaoService;

    @MockBean
    private SubcategoriaConsultaService consultaService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static ResultMatcher dtoJsonPath(Record dto) {
        return ROOT_JSON_PATH.value(dto);
    }

    private ResultMatcher pageJsonPath(Object o) {
        return ROOT_JSON_PATH.value(objectMapper.convertValue(o, JsonNode.class));
    }

    private String asString(Object dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    @Test
    void criarSubcategoriaComDadosValidosRetornaCreated() throws Exception {
        given(manutencaoService.criar(any(SubcategoriaRegistroDto.class))).willReturn(getSubcategoriaDetalhesDto());
        mockMvc
            .perform(post(ROOT_URL)
                .content(asString(getSubcategoriaRegistroDto()))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpectAll(dtoJsonPath(getSubcategoriaDetalhesDto()));
    }

    @Test
    void criarSubcategoriaComDadosInvalidosRetornaUnprocessableEntity() throws Exception {
        mockMvc
            .perform(post(ROOT_URL)
                .content(asString(getSubcategoriaInvalida()))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void criarSubcategoriaComCategoriaInexistenteRetornaNotFound() throws Exception {
        given(manutencaoService.criar(any(SubcategoriaRegistroDto.class))).willThrow(CategoriaNotFoundException.class);
        mockMvc
            .perform(post(ROOT_URL)
                .content(asString(getSubcategoriaRegistroDto()))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void criarSubcategoriaComNomeExistenteRetornaConflict() throws Exception {
        given(manutencaoService.criar(any(SubcategoriaRegistroDto.class)))
            .willThrow(SubcategoriaAlreadyExistsException.class);
        mockMvc
            .perform(post(ROOT_URL)
                .content(asString(getSubcategoriaRegistroDto()))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isConflict());
    }

    @Test
    void atualizarSubcategoriaComDadosValidosRetornaOk() throws Exception {
        given(manutencaoService.atualizar(anyLong(), any(SubcategoriaRegistroDto.class)))
            .willReturn(getSubcategoriaDetalhesDto());
        mockMvc
            .perform(put(ID_URL, 1)
                .content(asString(getSubcategoriaRegistroDto()))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(dtoJsonPath(getSubcategoriaDetalhesDto()));
    }

    @Test
    void atualizarSubcategoriaComDadosInvalidosRetornaUnprocessableEntity() throws Exception {
        mockMvc
            .perform(put(ID_URL, 1)
                .content(asString(getSubcategoriaInvalida()))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void atualizarSubcategoriaInexistenteRetornaNotFound() throws Exception {
        given(manutencaoService.atualizar(anyLong(), any(SubcategoriaRegistroDto.class)))
            .willThrow(SubcategoriaNotFoundException.class);
        mockMvc
            .perform(put(ID_URL, 1)
                .content(asString(getSubcategoriaRegistroDto()))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void atualizarSubcategoriaComCategoriaInexistenteRetornaNotFound() throws Exception {
        given(manutencaoService.atualizar(anyLong(), any(SubcategoriaRegistroDto.class)))
            .willThrow(CategoriaNotFoundException.class);
        mockMvc
            .perform(put(ID_URL, 1)
                .content(asString(getSubcategoriaRegistroDto()))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void removerSubcategoriaPorIdExistenteRetornaOk() throws Exception {
        given(manutencaoService.remover(anyLong())).willReturn("Subcategoria removida");
        mockMvc
            .perform(delete(ID_URL, 10))
            .andExpect(status().isOk())
            .andExpectAll(ROOT_JSON_PATH.value("Subcategoria removida"));
    }

    @Test
    void removerSubcategoriaPorIdInexistenteRetornaNotFound() throws Exception {
        given(manutencaoService.remover(anyLong())).willThrow(SubcategoriaNotFoundException.class);
        mockMvc
            .perform(delete(ID_URL, 10))
            .andExpectAll(status().isNotFound());
    }

    @Test
    void listarSubcategoriasExistentesRetornaOk() throws Exception {
        final var pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        final var listaSubcategorias = new ArrayList<SubcategoriaDto>();
        listaSubcategorias.add(getSubcategoriaDto());
        final var pageSubcategorias = new PageImpl<>(listaSubcategorias, pageable, 10);
        given(consultaService.listar(any(Pageable.class))).willReturn(pageSubcategorias);
        mockMvc
            .perform(get(ROOT_URL))
            .andExpect(status().isOk())
            .andExpect(pageJsonPath(pageSubcategorias));
    }

    @Test
    void listarSubcategoriasInexistentesRetornaNotFound() throws Exception {
        given(consultaService.listar(any(Pageable.class))).willThrow(SubcategoriaNotFoundException.class);
        mockMvc
            .perform(get(ROOT_URL))
            .andExpect(status().isNotFound());
    }

    @Test
    void consultarSubcategoriaPorIdExistenteRetornaOk() throws Exception {
        given(consultaService.consultarPorId(anyLong())).willReturn(getSubcategoriaDetalhesDto());
        mockMvc
            .perform(get(ID_URL, 1))
            .andExpect(status().isOk())
            .andExpect(dtoJsonPath(getSubcategoriaDetalhesDto()));
    }

    @Test
    void consultarSubcategoriaPorIdInexistenteRetornaNotFound() throws Exception {
        given(consultaService.consultarPorId(anyLong())).willThrow(SubcategoriaNotFoundException.class);
        mockMvc
            .perform(get(ID_URL, 10))
            .andExpect(status().isNotFound());
    }

    @Test
    void consultarSubcategoriaPorIdInvalidoRetornaBadRequest() throws Exception {
        mockMvc
            .perform(get(ID_URL, "a"))
            .andExpect(status().isBadRequest())
            .andExpect(ROOT_JSON_PATH.value("Parâmetro inválido. Verifique e tente novamente"));
    }

    @Test
    void criarSubcategoriaComRequestBodyNuloRetornaBadRequest() throws Exception {
        mockMvc
            .perform(post(ROOT_URL)
                .content(asString(null))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(ROOT_JSON_PATH.value("Informação inválida. Verifique os dados e tente novamente"));
    }

}
