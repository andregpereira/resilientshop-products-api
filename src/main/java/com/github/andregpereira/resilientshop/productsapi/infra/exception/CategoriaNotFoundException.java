package com.github.andregpereira.resilientshop.productsapi.infra.exception;

public class CategoriaNotFoundException extends RuntimeException {

    public CategoriaNotFoundException(String message) {
        super(message);
    }

}
