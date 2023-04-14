package com.github.andregpereira.resilientshop.productsapi.infra.exceptions;

public class ProdutoAlreadyExistsException extends RuntimeException {

    public ProdutoAlreadyExistsException(String message) {
        super(message);
    }

}
