package com.github.andregpereira.resilientshop.productsapi.infra.exception;

public class ProdutoNotFoundException extends RuntimeException {

    public ProdutoNotFoundException(String message) {
        super(message);
    }

}
