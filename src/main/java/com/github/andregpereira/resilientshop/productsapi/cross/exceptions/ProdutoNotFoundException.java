package com.github.andregpereira.resilientshop.productsapi.cross.exceptions;

public class ProdutoNotFoundException extends RuntimeException {

    public ProdutoNotFoundException(String message) {
        super(message);
    }

}
