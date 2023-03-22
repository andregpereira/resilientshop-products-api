package com.github.andregpereira.resilientshop.productsapi.services;

import java.security.InvalidParameterException;
import java.util.stream.Stream;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ServiceExceptionHandler {

	private record DadoInvalido(String campo, String mensagem) {
		public DadoInvalido(FieldError erro) {
			this(erro.getField(), erro.getDefaultMessage());
		}
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<String> erro400(HttpMessageNotReadableException e) {
		return ResponseEntity.badRequest().body("Informação inválida. Verifique os dados e tente novamente");
	}

	@ExceptionHandler(InvalidParameterException.class)
	public ResponseEntity<String> erro400(InvalidParameterException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Stream<DadoInvalido>> erro400(MethodArgumentNotValidException e) {
		Stream<FieldError> erros = e.getFieldErrors().stream();
		return ResponseEntity.badRequest().body(erros.map(DadoInvalido::new));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<String> erro400(MethodArgumentTypeMismatchException e) {
		return ResponseEntity.badRequest().body("Parâmetro inválido. Verifique e tente novamente");
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<DadoInvalido> erro400(MissingServletRequestParameterException e) {
		return ResponseEntity.badRequest()
				.body(new DadoInvalido(e.getParameterName(), "O campo " + e.getParameterName() + " é obrigatório"));
	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<String> erro404(EmptyResultDataAccessException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<String> erro404(EntityNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	private ResponseEntity<String> erro409(DataIntegrityViolationException e) {
		if (e.getMessage().contains("uc_sku")) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Produto com esse SKU já existe.");
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body("Dado inválido. Verifique e tente novamente");
	}

	@ExceptionHandler(EntityExistsException.class)
	public ResponseEntity<String> erro409(EntityExistsException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}

}
