package com.github.andregpereira.resilientshop.productsapi.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.produto.ProdutoDto;
import com.github.andregpereira.resilientshop.productsapi.app.services.produto.ProdutoConsultaService;
import com.github.andregpereira.resilientshop.productsapi.app.services.produto.ProdutoManutencaoService;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.ProdutoAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.ProdutoNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.SubcategoriaNotFoundException;
import jakarta.validation.ConstraintViolationException;
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

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.github.andregpereira.resilientshop.productsapi.constants.ProdutoDtoConstants.*;
import static com.github.andregpereira.resilientshop.productsapi.constants.SubcategoriaDtoConstants.SUBCATEGORIA_DTO_ATUALIZADA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @MockBean
    private ProdutoManutencaoService manutencaoService;

    @MockBean
    private ProdutoConsultaService consultaService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void criarProdutoComDadosValidosRetornaCreated() throws Exception {
        given(manutencaoService.registrar(PRODUTO_REGISTRO_DTO)).willReturn(PRODUTO_DETALHES_DTO);
        mockMvc.perform(post("/produtos").content(objectMapper.writeValueAsString(PRODUTO_REGISTRO_DTO)).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andExpectAll(jsonPath("$").exists(),
                jsonPath("$.sku").value(PRODUTO_DETALHES_DTO.sku()),
                jsonPath("$.nome").value(PRODUTO_DETALHES_DTO.nome()),
                jsonPath("$.descricao").value(PRODUTO_DETALHES_DTO.descricao()), jsonPath("$.dataCriacao").value(
                        PRODUTO_DETALHES_DTO.dataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm"))),
                jsonPath("$.valorUnitario").value(PRODUTO_DETALHES_DTO.valorUnitario()),
                jsonPath("$.estoque").value(PRODUTO_DETALHES_DTO.estoque()),
                jsonPath("$.subcategoria.nome").value(PRODUTO_DETALHES_DTO.subcategoria().nome()),
                jsonPath("$.subcategoria.descricao").value(PRODUTO_DETALHES_DTO.subcategoria().descricao()));
    }

    @Test
    void criarProdutoComDadosInvalidosRetornaUnprocessableEntity() throws Exception {
        mockMvc.perform(post("/produtos").content(
                objectMapper.writeValueAsString(PRODUTO_REGISTRO_DTO_INVALIDO)).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
    }

    @Test
    void criarProdutoComSubcategoriaInexistenteRetornaNotFound() throws Exception {
        given(manutencaoService.registrar(PRODUTO_REGISTRO_DTO)).willThrow(SubcategoriaNotFoundException.class);
        mockMvc.perform(post("/produtos").content(objectMapper.writeValueAsString(PRODUTO_REGISTRO_DTO)).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void criarProdutoComSkuOuNomeExistentesRetornaConflict() throws Exception {
        given(manutencaoService.registrar(PRODUTO_REGISTRO_DTO)).willThrow(ProdutoAlreadyExistsException.class);
        mockMvc.perform(post("/produtos").content(objectMapper.writeValueAsString(PRODUTO_REGISTRO_DTO)).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isConflict());
    }

    //    @Test
//     void criarProdutoComNomeExistenteRetornaConflict() throws Exception {
//        given(manutencaoService.registrar(PRODUTO_REGISTRO_DTO)).willThrow(ProdutoAlreadyExistsException.class);
//        mockMvc.perform(post("/produtos").content(
//                objectMapper.writeValueAsString(PRODUTO_REGISTRO_DTO)).contentType(
//                MediaType.APPLICATION_JSON)).andExpect(status().isConflict());
//    }
    @Test
    void atualizarProdutoComDadosValidosRetornaOkEProdutoDetalhesDto() throws Exception {
        given(manutencaoService.atualizar(1L, PRODUTO_ATUALIZACAO_DTO)).willReturn(PRODUTO_DETALHES_DTO_ATUALIZADO);
        mockMvc.perform(put("/produtos/1").content(
                objectMapper.writeValueAsString(PRODUTO_ATUALIZACAO_DTO)).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpectAll(jsonPath("$").exists(),
                jsonPath("$.sku").value(PRODUTO_DETALHES_DTO_ATUALIZADO.sku()),
                jsonPath("$.nome").value(PRODUTO_DETALHES_DTO_ATUALIZADO.nome()),
                jsonPath("$.descricao").value(PRODUTO_DETALHES_DTO_ATUALIZADO.descricao()),
                jsonPath("$.dataCriacao").value(PRODUTO_DETALHES_DTO_ATUALIZADO.dataCriacao().format(
                        DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm"))),
                jsonPath("$.valorUnitario").value(PRODUTO_DETALHES_DTO_ATUALIZADO.valorUnitario()),
                jsonPath("$.estoque").value(PRODUTO_DETALHES_DTO_ATUALIZADO.estoque()),
                jsonPath("$.subcategoria.nome").value(SUBCATEGORIA_DTO_ATUALIZADA.nome()),
                jsonPath("$.subcategoria.descricao").value(SUBCATEGORIA_DTO_ATUALIZADA.descricao()));
    }

    @Test
    void atualizarProdutoComDadosInvalidosRetornaUnprocessableEntity() throws Exception {
        mockMvc.perform(put("/produtos/1").content(
                objectMapper.writeValueAsString(PRODUTO_ATUALIZACAO_DTO_INVALIDO)).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
    }

    @Test
    void atualizarProdutoInexistenteRetornaNotFound() throws Exception {
        given(manutencaoService.atualizar(1L, PRODUTO_ATUALIZACAO_DTO)).willThrow(ProdutoNotFoundException.class);
        mockMvc.perform(put("/produtos/1").content(
                objectMapper.writeValueAsString(PRODUTO_ATUALIZACAO_DTO)).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void removerProdutoPorIdExistenteRetornaOk() throws Exception {
        given(manutencaoService.remover(10L)).willReturn("Produto com id 10 removido com sucesso");
        mockMvc.perform(delete("/produtos/10")).andExpect(status().isOk()).andExpectAll(
                jsonPath("$").value("Produto com id 10 removido com sucesso"));
    }

    @Test
    void removerProdutoPorIdInexistenteRetornaNotFound() throws Exception {
        given(manutencaoService.remover(10L)).willThrow(ProdutoNotFoundException.class);
        mockMvc.perform(delete("/produtos/10")).andExpectAll(status().isNotFound());
    }

    @Test
    void listarProdutosExistentesRetornaOk() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        List<ProdutoDto> listaProdutos = new ArrayList<>();
        listaProdutos.add(PRODUTO_DTO);
        listaProdutos.add(PRODUTO_DTO_ATUALIZADO);
        Page<ProdutoDto> pageProdutos = new PageImpl<>(listaProdutos, pageable, 10);
        given(consultaService.listar(pageable)).willReturn(pageProdutos);
        mockMvc.perform(get("/produtos")).andExpect(status().isOk()).andExpectAll(jsonPath("$.empty").value(false),
                jsonPath("$.numberOfElements").value(2));
    }

    @Test
    void listarProdutosInexistentesRetornaNotFound() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        given(consultaService.listar(pageable)).willThrow(ProdutoNotFoundException.class);
        mockMvc.perform(get("/produtos")).andExpect(status().isNotFound());
    }

    @Test
    void consultarProdutoPorIdExistenteRetornaProdutoDetalhesDto() throws Exception {
        given(consultaService.consultarPorId(1L)).willReturn(PRODUTO_DETALHES_DTO);
        mockMvc.perform(get("/produtos/1")).andExpect(status().isOk()).andExpectAll(
                jsonPath("$.nome").value(PRODUTO_DETALHES_DTO.nome()));
    }

    @Test
    void consultarProdutoPorIdInexistenteRetornaNotFound() throws Exception {
        given(consultaService.consultarPorId(10L)).willThrow(ProdutoNotFoundException.class);
        mockMvc.perform(get("/produtos/10")).andExpect(status().isNotFound());
    }

    @Test
    void consultarProdutoPorIdInvalidoRetornaBadRequest() throws Exception {
        mockMvc.perform(get("/produtos/a")).andExpect(status().isBadRequest()).andExpectAll(
                jsonPath("$").value("Parâmetro inválido. Verifique e tente novamente"));
    }

    @Test
    void consultarProdutoPorNomeExistenteRetornaProdutoDto() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "nome");
        List<ProdutoDto> listaProdutos = new ArrayList<>();
        listaProdutos.add(PRODUTO_DTO);
        Page<ProdutoDto> pageProdutos = new PageImpl<>(listaProdutos, pageable, 10);
        given(consultaService.consultarPorNome("nome", pageable)).willReturn(pageProdutos);
        mockMvc.perform(get("/produtos/nome").param("nome", "nome")).andExpect(status().isOk()).andExpectAll(
                jsonPath("$").exists(), jsonPath("$.empty").value(false), jsonPath("$.numberOfElements").value(1));
    }

    @Test
    void consultarProdutoPorNomeInexistenteRetornaNotFound() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "nome");
        given(consultaService.consultarPorNome("nomeProduto", pageable)).willThrow(ProdutoNotFoundException.class);
        mockMvc.perform(get("/produtos/nome").param("nome", "nomeProduto")).andExpect(status().isNotFound());
    }

    @Test
    void consultarProdutoPorSubcategoriaExistenteRetornaProdutoDto() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "nome");
        List<ProdutoDto> listaProdutos = new ArrayList<>();
        listaProdutos.add(PRODUTO_DTO);
        Page<ProdutoDto> pageProdutos = new PageImpl<>(listaProdutos, pageable, 10);
        given(consultaService.consultarPorSubcategoria(1L, pageable)).willReturn(pageProdutos);
        mockMvc.perform(get("/produtos/subcategoria/1")).andExpect(status().isOk()).andExpectAll(jsonPath("$").exists(),
                jsonPath("$.empty").value(false), jsonPath("$.numberOfElements").value(1));
    }

    @Test
    void consultarProdutoPorSubcategoriaInexistenteRetornaNotFound() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "nome");
        given(consultaService.consultarPorSubcategoria(20L, pageable)).willThrow(ProdutoNotFoundException.class);
        mockMvc.perform(get("/produtos/subcategoria/20")).andExpect(status().isNotFound());
    }

    @Test
    void consultarProdutoPorCategoriaExistenteRetornaProdutoDto() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "nome");
        List<ProdutoDto> listaProdutos = new ArrayList<>();
        listaProdutos.add(PRODUTO_DTO);
        Page<ProdutoDto> pageProdutos = new PageImpl<>(listaProdutos, pageable, 10);
        given(consultaService.consultarPorCategoria(5L, pageable)).willReturn(pageProdutos);
        mockMvc.perform(get("/produtos/categoria/5")).andExpect(status().isOk()).andExpectAll(jsonPath("$").exists(),
                jsonPath("$.empty").value(false), jsonPath("$.numberOfElements").value(1));
    }

    @Test
    void consultarProdutoPorCategoriaInexistenteRetornaNotFound() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "nome");
        given(consultaService.consultarPorCategoria(25L, pageable)).willThrow(ProdutoNotFoundException.class);
        mockMvc.perform(get("/produtos/categoria/25")).andExpect(status().isNotFound());
    }

    @Test
    void criarProdutoComRequestBodyNuloRetornaBadRequest() throws Exception {
        mockMvc.perform(post("/produtos").content(objectMapper.writeValueAsString(null)).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andExpectAll(
                jsonPath("$").value("Informação inválida. Verifique os dados e tente novamente"));
    }

    @Test
    void inserirParametroNomeVazioThrowsException() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "nome");
        given(consultaService.consultarPorNome("", pageable)).willThrow(ConstraintViolationException.class);
        mockMvc.perform(get("/produtos/nome").param("nome", "")).andExpect(status().isBadRequest()).andExpectAll(
                jsonPath("$[0].campo").value("nome"),
                jsonPath("$[0].mensagem").value("O nome deve ter pelo menos 2 caracteres"));
    }

    @Test
    void inserirParametroNomeNuloThrowsException() throws Exception {
        mockMvc.perform(get("/produtos/nome")).andExpect(status().isBadRequest()).andExpectAll(
                jsonPath("$.campo").value("nome"), jsonPath("$.mensagem").value("O campo nome é obrigatório"));
    }

}
