package com.github.andregpereira.resilientshop.productsapi.cross.exceptions;

public class ProdutoAlreadyExistsException extends RuntimeException {

    public ProdutoAlreadyExistsException(String message) {
        super(message);
    }

}
