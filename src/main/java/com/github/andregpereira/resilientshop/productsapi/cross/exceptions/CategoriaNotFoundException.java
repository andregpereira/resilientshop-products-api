package com.github.andregpereira.resilientshop.productsapi.cross.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoriaNotFoundException extends RuntimeException {

    public CategoriaNotFoundException(String message) {
        super(message);
    }

    public CategoriaNotFoundException(Long id) {
        super(MessageFormat.format("Ops! Não foi encontrada uma categoria com o id {0}", id));
    }

}
