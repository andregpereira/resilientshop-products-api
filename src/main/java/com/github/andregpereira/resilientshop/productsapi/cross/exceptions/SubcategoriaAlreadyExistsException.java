package com.github.andregpereira.resilientshop.productsapi.cross.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(HttpStatus.CONFLICT)
public class SubcategoriaAlreadyExistsException extends RuntimeException {

    public SubcategoriaAlreadyExistsException(String nome) {
        super(MessageFormat.format("Poxa! Já existe uma subcategoria cadastrada com o nome {0}", nome));
    }

}
