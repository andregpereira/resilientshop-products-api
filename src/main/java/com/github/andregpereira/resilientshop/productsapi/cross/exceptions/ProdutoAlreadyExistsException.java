package com.github.andregpereira.resilientshop.productsapi.cross.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(HttpStatus.CONFLICT)
public class ProdutoAlreadyExistsException extends RuntimeException {

    public ProdutoAlreadyExistsException(Long sku) {
        super(MessageFormat.format("Opa! Já existe um produto cadastrado com o SKU {0}",
                sku.toString().replace(".", "")));
    }

    public ProdutoAlreadyExistsException(String nome) {
        super(MessageFormat.format("Opa! Já existe um produto cadastrado com o nome {0}", nome));
    }

}
