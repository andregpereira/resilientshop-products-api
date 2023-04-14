package com.github.andregpereira.resilientshop.productsapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.andregpereira.resilientshop.productsapi.app.controllers.SubcategoriaController;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.subcategoria.SubcategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.infra.exceptions.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.infra.exceptions.SubcategoriaAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.infra.exceptions.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.app.services.subcategoria.SubcategoriaConsultaService;
import com.github.andregpereira.resilientshop.productsapi.app.services.subcategoria.SubcategoriaManutencaoService;
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

import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaDtoConstants.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubcategoriaController.class)
class SubcategoriaControllerTest {

    @MockBean
    private SubcategoriaManutencaoService manutencaoService;

    @MockBean
    private SubcategoriaConsultaService consultaService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void criarSubcategoriaComDadosValidosRetornaCreated() throws Exception {
        given(manutencaoService.registrar(SUBCATEGORIA_REGISTRO_DTO)).willReturn(SUBCATEGORIA_DETALHES_DTO);
        mockMvc.perform(post("/subcategorias").content(
                objectMapper.writeValueAsString(SUBCATEGORIA_REGISTRO_DTO)).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andExpectAll(jsonPath("$").exists(),
                jsonPath("$.nome").value(SUBCATEGORIA_DETALHES_DTO.nome()),
                jsonPath("$.descricao").value(SUBCATEGORIA_DETALHES_DTO.descricao()),
                jsonPath("$.categoria.nome").value(SUBCATEGORIA_DETALHES_DTO.categoria().nome()));
    }

    @Test
    void criarSubcategoriaComDadosInvalidosRetornaUnprocessableEntity() throws Exception {
        mockMvc.perform(post("/subcategorias").content(
                objectMapper.writeValueAsString(SUBCATEGORIA_REGISTRO_DTO_INVALIDA)).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
    }

    @Test
    void criarSubcategoriaComCategoriaInexistenteRetornaNotFound() throws Exception {
        given(manutencaoService.registrar(SUBCATEGORIA_REGISTRO_DTO)).willThrow(CategoriaNotFoundException.class);
        mockMvc.perform(post("/subcategorias").content(
                objectMapper.writeValueAsString(SUBCATEGORIA_REGISTRO_DTO)).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void criarSubcategoriaComNomeExistenteRetornaConflict() throws Exception {
        given(manutencaoService.registrar(SUBCATEGORIA_REGISTRO_DTO)).willThrow(
                SubcategoriaAlreadyExistsException.class);
        mockMvc.perform(post("/subcategorias").content(
                objectMapper.writeValueAsString(SUBCATEGORIA_REGISTRO_DTO)).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isConflict());
    }

    @Test
    void atualizarSubcategoriaComDadosValidosRetornaOk() throws Exception {
        given(manutencaoService.atualizar(1L, SUBCATEGORIA_REGISTRO_DTO_ATUALIZADA)).willReturn(
                SUBCATEGORIA_DETALHES_DTO_ATUALIZADA);
        mockMvc.perform(put("/subcategorias/1").content(
                objectMapper.writeValueAsString(SUBCATEGORIA_REGISTRO_DTO_ATUALIZADA)).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpectAll(jsonPath("$").exists(),
                jsonPath("$.nome").value(SUBCATEGORIA_DETALHES_DTO_ATUALIZADA.nome()),
                jsonPath("$.descricao").value(SUBCATEGORIA_DETALHES_DTO_ATUALIZADA.descricao()),
                jsonPath("$.categoria.nome").value(SUBCATEGORIA_DETALHES_DTO_ATUALIZADA.categoria().nome()));
    }

    @Test
    void atualizarSubcategoriaComDadosInvalidosRetornaUnprocessableEntity() throws Exception {
        mockMvc.perform(put("/subcategorias/1").content(
                objectMapper.writeValueAsString(SUBCATEGORIA_REGISTRO_DTO_ATUALIZADO_INVALIDA)).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
    }

    @Test
    void atualizarSubcategoriaInexistenteRetornaNotFound() throws Exception {
        given(manutencaoService.atualizar(1L, SUBCATEGORIA_REGISTRO_DTO_ATUALIZADA)).willThrow(
                SubcategoriaNotFoundException.class);
        mockMvc.perform(put("/subcategorias/1").content(
                objectMapper.writeValueAsString(SUBCATEGORIA_REGISTRO_DTO_ATUALIZADA)).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void atualizarSubcategoriaComCategoriaInexistenteRetornaNotFound() throws Exception {
        given(manutencaoService.atualizar(1L, SUBCATEGORIA_REGISTRO_DTO)).willThrow(CategoriaNotFoundException.class);
        mockMvc.perform(put("/subcategorias/1").content(
                objectMapper.writeValueAsString(SUBCATEGORIA_REGISTRO_DTO)).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void removerSubcategoriaPorIdExistenteRetornaOk() throws Exception {
        given(manutencaoService.remover(10L)).willReturn("Subcategoria removida");
        mockMvc.perform(delete("/subcategorias/10")).andExpect(status().isOk()).andExpectAll(
                jsonPath("$").value("Subcategoria removida"));
    }

    @Test
    void removerSubcategoriaPorIdInexistenteRetornaNotFound() throws Exception {
        given(manutencaoService.remover(10L)).willThrow(SubcategoriaNotFoundException.class);
        mockMvc.perform(delete("/subcategorias/10")).andExpectAll(status().isNotFound());
    }

    @Test
    void listarSubcategoriasExistentesRetornaOk() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        List<SubcategoriaDto> listaSubcategorias = new ArrayList<>();
        listaSubcategorias.add(SUBCATEGORIA_DTO);
        Page<SubcategoriaDto> pageSubcategorias = new PageImpl<>(listaSubcategorias, pageable, 10);
        given(consultaService.listar(pageable)).willReturn(pageSubcategorias);
        mockMvc.perform(get("/subcategorias")).andExpect(status().isOk()).andExpectAll(jsonPath("$").exists(),
                jsonPath("$.empty").value(false), jsonPath("$.numberOfElements").value(1));
    }

    @Test
    void listarSubcategoriasInexistentesRetornaNotFound() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        given(consultaService.listar(pageable)).willThrow(SubcategoriaNotFoundException.class);
        mockMvc.perform(get("/subcategorias")).andExpect(status().isNotFound());
    }

    @Test
    void consultarSubcategoriaPorIdExistenteRetornaOk() throws Exception {
        given(consultaService.consultarPorId(1L)).willReturn(SUBCATEGORIA_DETALHES_DTO);
        mockMvc.perform(get("/subcategorias/1")).andExpect(status().isOk()).andExpectAll(jsonPath("$").exists(),
                jsonPath("$.nome").value(SUBCATEGORIA_DETALHES_DTO.nome()),
                jsonPath("$.descricao").value(SUBCATEGORIA_DETALHES_DTO.descricao()),
                jsonPath("$.categoria.nome").value(SUBCATEGORIA_DETALHES_DTO.categoria().nome()));
    }

    @Test
    void consultarSubcategoriaPorIdInexistenteRetornaNotFound() throws Exception {
        given(consultaService.consultarPorId(10L)).willThrow(SubcategoriaNotFoundException.class);
        mockMvc.perform(get("/subcategorias/10")).andExpect(status().isNotFound());
    }

    @Test
    void consultarSubcategoriaPorIdInvalidoRetornaBadRequest() throws Exception {
        mockMvc.perform(get("/subcategorias/a")).andExpect(status().isBadRequest()).andExpectAll(
                jsonPath("$").value("Parâmetro inválido. Verifique e tente novamente"));
    }

    @Test
    void criarSubcategoriaComRequestBodyNuloRetornaBadRequest() throws Exception {
        mockMvc.perform(post("/subcategorias").content(objectMapper.writeValueAsString(null)).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andExpectAll(
                jsonPath("$").value("Informação inválida. Verifique os dados e tente novamente"));
    }

}
