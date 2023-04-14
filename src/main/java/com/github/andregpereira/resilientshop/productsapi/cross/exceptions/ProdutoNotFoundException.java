package com.github.andregpereira.resilientshop.productsapi.infra.exceptions;

public class ProdutoNotFoundException extends RuntimeException {

    public ProdutoNotFoundException(String message) {
        super(message);
    }

}
