package com.github.andregpereira.resilientshop.productsapi.infra.exceptions;

public class CategoriaAlreadyExistsException extends RuntimeException {

    public CategoriaAlreadyExistsException(String message) {
        super(message);
    }

}
