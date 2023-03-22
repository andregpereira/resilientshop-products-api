package com.github.andregpereira.resilientshop.productsapi.infra.exception;

public class CategoriaAlreadyExistsException extends RuntimeException {

    public CategoriaAlreadyExistsException(String message) {
        super(message);
    }

}
