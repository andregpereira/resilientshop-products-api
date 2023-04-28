package com.github.andregpereira.resilientshop.productsapi.cross.exceptions;

import java.text.MessageFormat;

public class ProdutoNotFoundException extends RuntimeException {

    public ProdutoNotFoundException(String message) {
        super(message);
    }

    public ProdutoNotFoundException(Long id) {
        super(MessageFormat.format("Poxa! Nenhum produto foi encontrado com o id {0}", id));
    }

}
