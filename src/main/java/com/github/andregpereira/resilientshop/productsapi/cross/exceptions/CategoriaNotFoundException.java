package com.github.andregpereira.resilientshop.productsapi.cross.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoriaNotFoundException extends RuntimeException {

    public CategoriaNotFoundException(Long id) {
        super(MessageFormat.format("Ops! Nenhuma categoria foi encontrada com o id {0}", id));
    }

}
