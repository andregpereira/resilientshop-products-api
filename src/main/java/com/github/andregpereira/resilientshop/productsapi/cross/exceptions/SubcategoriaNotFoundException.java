package com.github.andregpereira.resilientshop.productsapi.cross.exceptions;

import java.text.MessageFormat;

public class SubcategoriaNotFoundException extends RuntimeException {

    public SubcategoriaNotFoundException(String message) {
        super(message);
    }

    public SubcategoriaNotFoundException(Long id) {
        super(MessageFormat.format("Ops! Não foi possível encontrar uma subcategoria com o id {0}", id));
    }

}
