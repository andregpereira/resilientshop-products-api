package com.github.andregpereira.resilientshop.productsapi.infra.exceptions;

public class SubcategoriaAlreadyExistsException extends RuntimeException {

    public SubcategoriaAlreadyExistsException(String message) {
        super(message);
    }

}
