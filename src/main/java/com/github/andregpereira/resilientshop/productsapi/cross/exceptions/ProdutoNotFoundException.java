package com.github.andregpereira.resilientshop.productsapi.cross.exceptions;

import java.text.MessageFormat;

public class ProdutoNotFoundException extends RuntimeException {

    public ProdutoNotFoundException() {
        super("Poxa! Ainda não há produtos cadastrados");
    }

    public ProdutoNotFoundException(String mensagem) {
        super(mensagem);
    }

    public ProdutoNotFoundException(String atributo, String valor) {
        super(MessageFormat.format("Opa! Nenhum produto foi encontrado com o/a {0} {1}", atributo, valor));
    }

    public ProdutoNotFoundException(Long id) {
        super(MessageFormat.format("Ops! Nenhum produto foi encontrado com o id {0}", id));
    }

}
