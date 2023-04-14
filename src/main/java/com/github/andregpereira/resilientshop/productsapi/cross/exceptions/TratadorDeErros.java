package com.github.andregpereira.resilientshop.productsapi.cross.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.security.InvalidParameterException;
import java.util.stream.Stream;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> erro400(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body("Informação inválida. Verifique os dados e tente novamente");
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<String> erro400(InvalidParameterException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> erro400(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.badRequest().body("Parâmetro inválido. Verifique e tente novamente");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<DadoInvalido> erro400(MissingServletRequestParameterException e) {
        return ResponseEntity.badRequest().body(
                new DadoInvalido(e.getParameterName(), "O campo " + e.getParameterName() + " é obrigatório"));
    }

    @ExceptionHandler(ProdutoNotFoundException.class)
    public ResponseEntity<String> erro404(ProdutoNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(SubcategoriaNotFoundException.class)
    public ResponseEntity<String> erro404(SubcategoriaNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(CategoriaNotFoundException.class)
    public ResponseEntity<String> erro404(CategoriaNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ProdutoAlreadyExistsException.class)
    public ResponseEntity<String> erro409(ProdutoAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(SubcategoriaAlreadyExistsException.class)
    public ResponseEntity<String> erro409(SubcategoriaAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(CategoriaAlreadyExistsException.class)
    public ResponseEntity<String> erro409(CategoriaAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Stream<DadoInvalido>> erro422(MethodArgumentNotValidException e) {
        Stream<FieldError> erros = e.getFieldErrors().stream();
        return ResponseEntity.unprocessableEntity().body(erros.map(DadoInvalido::new));
    }

    private record DadoInvalido(String campo,
            String mensagem) {

        public DadoInvalido(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }

    }

}
