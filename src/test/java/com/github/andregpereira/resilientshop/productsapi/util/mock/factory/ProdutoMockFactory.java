package com.github.andregpereira.resilientshop.productsapi.util.mock.factory;

import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoAtualizacaoDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoAtualizarEstoqueDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.produto.ProdutoRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.ProdutoEntity;
import com.github.andregpereira.resilientshop.productsapi.util.constant.CommonConstants;
import com.github.andregpereira.resilientshop.productsapi.util.json.JsonReader;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static com.github.andregpereira.resilientshop.productsapi.util.constant.CommonConstants.PAGEABLE_ID;
import static com.github.andregpereira.resilientshop.productsapi.util.constant.CommonConstants.PAGEABLE_NOME;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.InvalidObjectMockFactory.readInvalidJson;

@UtilityClass
public class ProdutoMockFactory {

    private static final String MOCKS_PATH = CommonConstants.MOCKS_PATH.concat("/produto");

    private static <T> T readJson(final String filePath, final Class<T> clazz) {
        return JsonReader.read(MOCKS_PATH.concat(filePath), clazz);
    }

    public static ProdutoDto getProdutoDto() {
        return readJson("/produto.json", ProdutoDto.class);
    }

    public static ProdutoDetalhesDto getProdutoDetalhesDto() {
        return readJson("/produto.json", ProdutoDetalhesDto.class);
    }

    public static ProdutoRegistroDto getProdutoRegistroDto() {
        return readJson("/produto-registro.json", ProdutoRegistroDto.class);
    }

    public static Object jsonInvalido() {
        return readInvalidJson();
    }

    public static ProdutoEntity getProdutoRegistro() {
        return readJson("/produto-registro.json", ProdutoEntity.class);
    }

    public static ProdutoAtualizacaoDto getProdutoAtualizacaoDto() {
        return readJson("/produto-registro.json", ProdutoAtualizacaoDto.class);
    }

    public static ProdutoAtualizarEstoqueDto getProdutoAtualizarEstoqueDto() {
        return readJson("/produto-atualizar-estoque.json", ProdutoAtualizarEstoqueDto.class);
    }

    public static ProdutoEntity getProdutoEntity() {
        return readJson("/produto.json", ProdutoEntity.class);
    }

    public static ProdutoEntity getProdutoEntityInvalido() {
        return readInvalidJson(ProdutoEntity.class);
    }

    public static Page<ProdutoDto> getPageIdProdutos() {
        return new PageImpl<>(getListaProdutos(), PAGEABLE_ID, 10);
    }

    public static Page<ProdutoDto> getPageNomeProdutos() {
        return new PageImpl<>(getListaProdutos(), PAGEABLE_NOME, 10);
    }

    private static List<ProdutoDto> getListaProdutos() {
        return List.of(getProdutoDto(), getProdutoDto());
    }

}
