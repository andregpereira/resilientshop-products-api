package com.github.andregpereira.resilientshop.productsapi.cross.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(HttpStatus.CONFLICT)
public class CategoriaAlreadyExistsException extends RuntimeException {

    public CategoriaAlreadyExistsException(String nome) {
        super(MessageFormat.format("Opa! Já existe uma categoria cadastrada com o nome {0}", nome));
    }

}
