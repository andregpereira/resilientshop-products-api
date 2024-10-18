package com.github.andregpereira.resilientshop.productsapi.cross.exceptions;

import java.text.MessageFormat;

public class SubcategoriaNotFoundException extends RuntimeException {

    public SubcategoriaNotFoundException(Long id) {
        super(MessageFormat.format("Ops! Nenhuma subcategoria foi encontrada com o id {0}", id));
    }

}
