package com.github.andregpereira.resilientshop.productsapi.infra.exceptions;

public class CategoriaNotFoundException extends RuntimeException {

    public CategoriaNotFoundException(String message) {
        super(message);
    }

}
