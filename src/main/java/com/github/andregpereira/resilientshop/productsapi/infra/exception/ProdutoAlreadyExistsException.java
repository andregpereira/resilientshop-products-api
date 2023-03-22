package com.github.andregpereira.resilientshop.productsapi.infra.exception;

public class ProdutoAlreadyExistsException extends RuntimeException {

    public ProdutoAlreadyExistsException(String message) {
        super(message);
    }

}
