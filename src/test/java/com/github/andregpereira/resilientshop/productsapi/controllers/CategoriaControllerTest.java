package com.github.andregpereira.resilientshop.productsapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.infra.exception.CategoriaAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.infra.exception.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.infra.exception.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.services.categoria.CategoriaConsultaService;
import com.github.andregpereira.resilientshop.productsapi.services.categoria.CategoriaManutencaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.github.andregpereira.resilientshop.productsapi.constants.CategoriaDtoConstants.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoriaController.class)
class CategoriaControllerTest {

    @MockBean
    private CategoriaManutencaoService manutencaoService;

    @MockBean
    private CategoriaConsultaService consultaService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void criarCategoriaComDadosValidosRetornaCreated() throws Exception {
        given(manutencaoService.registrar(CATEGORIA_REGISTRO_DTO)).willReturn(CATEGORIA_DTO);
        mockMvc.perform(post("/categorias").content(
                objectMapper.writeValueAsString(CATEGORIA_REGISTRO_DTO)).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andExpectAll(
                jsonPath("$.nome").value(CATEGORIA_DTO.nome()));
    }

    @Test
    void criarCategoriaComDadosInvalidosRetornaUnprocessableEntity() throws Exception {
        mockMvc.perform(post("/categorias").content(
                objectMapper.writeValueAsString(CATEGORIA_REGISTRO_DTO_INVALIDA)).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
    }

    @Test
    void criarSubcategoriaComNomeExistenteRetornaConflict() throws Exception {
        given(manutencaoService.registrar(CATEGORIA_REGISTRO_DTO)).willThrow(CategoriaAlreadyExistsException.class);
        mockMvc.perform(post("/categorias").content(
                objectMapper.writeValueAsString(CATEGORIA_REGISTRO_DTO)).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isConflict());
    }

    @Test
    void atualizarCategoriaComDadosValidosRetornaOk() throws Exception {
        given(manutencaoService.atualizar(1L, CATEGORIA_REGISTRO_DTO_ATUALIZADA)).willReturn(CATEGORIA_DTO_ATUALIZADA);
        mockMvc.perform(put("/categorias/1").content(
                objectMapper.writeValueAsString(CATEGORIA_REGISTRO_DTO_ATUALIZADA)).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpectAll(
                jsonPath("$.nome").value(CATEGORIA_DTO_ATUALIZADA.nome()));
    }

    @Test
    void atualizarCategoriaComDadosInvalidosRetornaUnprocessableEntity() throws Exception {
        mockMvc.perform(put("/categorias/1").content(
                objectMapper.writeValueAsString(CATEGORIA_REGISTRO_DTO_INVALIDA)).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
    }

    @Test
    void atualizarCategoriaInexistenteRetornaNotFound() throws Exception {
        given(manutencaoService.atualizar(1L, CATEGORIA_REGISTRO_DTO_ATUALIZADA)).willThrow(
                CategoriaNotFoundException.class);
        mockMvc.perform(put("/categorias/1").content(
                objectMapper.writeValueAsString(CATEGORIA_REGISTRO_DTO_ATUALIZADA)).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void removerCategoriaPorIdExistenteRetornaOk() throws Exception {
        given(manutencaoService.remover(10L)).willReturn("Categoria removida");
        mockMvc.perform(delete("/categorias/10")).andExpect(status().isOk()).andExpectAll(
                jsonPath("$").value("Categoria removida"));
    }

    @Test
    void removerCategoriaPorIdInexistenteRetornaNotFound() throws Exception {
        given(manutencaoService.remover(10L)).willThrow(CategoriaNotFoundException.class);
        mockMvc.perform(delete("/categorias/10")).andExpectAll(status().isNotFound());
    }

    @Test
    void listarCategoriasExistentesRetornaOk() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        List<CategoriaDto> listaCategorias = new ArrayList<>();
        listaCategorias.add(CATEGORIA_DTO);
        listaCategorias.add(CATEGORIA_DTO_ATUALIZADA);
        Page<CategoriaDto> pageCategorias = new PageImpl<>(listaCategorias, pageable, 10);
        given(consultaService.listar(pageable)).willReturn(pageCategorias);
        mockMvc.perform(get("/categorias")).andExpect(status().isOk()).andExpectAll(jsonPath("$.empty").value(false),
                jsonPath("$.numberOfElements").value(2));
    }

    @Test
    void listarCategoriasInexistentesRetornaNotFound() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        given(consultaService.listar(pageable)).willThrow(CategoriaNotFoundException.class);
        mockMvc.perform(get("/categorias")).andExpect(status().isNotFound());
    }

    @Test
    void consultarCategoriaPorIdExistenteRetornaOk() throws Exception {
        given(consultaService.consultarPorId(1L)).willReturn(CATEGORIA_DTO);
        mockMvc.perform(get("/categorias/1")).andExpect(status().isOk()).andExpectAll(
                jsonPath("$.nome").value(CATEGORIA_DTO.nome()));
    }

    @Test
    void consultarCategoriaPorIdInexistenteRetornaNotFound() throws Exception {
        given(consultaService.consultarPorId(10L)).willThrow(SubcategoriaNotFoundException.class);
        mockMvc.perform(get("/subcategorias/10")).andExpect(status().isNotFound());
    }

    @Test
    void consultarCategoriaPorIdInvalidoRetornaBadRequest() throws Exception {
        mockMvc.perform(get("/categorias/a")).andExpect(status().isBadRequest()).andExpectAll(
                jsonPath("$").value("Parâmetro inválido. Verifique e tente novamente"));
    }

    @Test
    void criarCategoriaComRequestBodyNuloRetornaBadRequest() throws Exception {
        mockMvc.perform(post("/categorias").content(objectMapper.writeValueAsString(null)).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andExpectAll(
                jsonPath("$").value("Informação inválida. Verifique os dados e tente novamente"));
    }

}
