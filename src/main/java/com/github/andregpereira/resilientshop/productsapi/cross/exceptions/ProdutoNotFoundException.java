package com.github.andregpereira.resilientshop.productsapi.cross.exceptions;

import java.text.MessageFormat;

public class ProdutoNotFoundException extends RuntimeException {

    public ProdutoNotFoundException(String mensagem) {
        super(mensagem);
    }

    public ProdutoNotFoundException(Long id) {
        super(MessageFormat.format("Ops! Nenhum produto foi encontrado com o id {0}", id));
    }

    public ProdutoNotFoundException(Long id, boolean ativo) {
        super(MessageFormat.format(
            "Ops! Não foi encontrado um produto {1} com o id {0}",
            id,
            ativo
                ? "ativo"
                : "inativo"
        ));
    }

}
