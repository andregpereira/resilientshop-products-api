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

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ProdutoServiceExceptionHandler {

	private record DadoInvalido(String campo, String mensagem) {
		public DadoInvalido(FieldError erro) {
			this(erro.getField(), erro.getDefaultMessage());
		}
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Stream<DadoInvalido>> erro400(MethodArgumentNotValidException e) {
		Stream<FieldError> erros = e.getFieldErrors().stream();
		return ResponseEntity.badRequest().body(erros.map(DadoInvalido::new));
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<String> erro400(MissingServletRequestParameterException e) {
//		if (e.getMessage().contains("nome")) {
//			return ResponseEntity.badRequest()
//					.body("Não foi possível realizar a busca por nome. Digite um nome e tente novamente.");
//		}
		return ResponseEntity.badRequest().body(
				"Não foi possível realizar a busca por produto. Por favor, preencha os campos obrigatórios e tente novamente.");
	}

	@ExceptionHandler(InvalidParameterException.class)
	public ResponseEntity<String> erro400(InvalidParameterException e) {
		if (e.getMessage().contains("produto_consulta_nome_tamanho_invalido")) {
			return ResponseEntity.badRequest().body(
					"Não foi possível realizar a busca por nome. O nome informado deve ter, pelo menos, 2 caracteres.");
		} else if (e.getMessage().contains("produto_consulta_nome_em_branco")) {
			return ResponseEntity.badRequest()
					.body("Não foi possível realizar a busca por nome. Digite um nome e tente novamente.");
		}
		return ResponseEntity.badRequest().body(
				"Não foi possível realizar a busca por produto. Por favor, preencha os campos obrigatórios e tente novamente.");
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<String> erro400(HttpMessageNotReadableException e) {
		return ResponseEntity.badRequest().body("Houve um erro.");
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<String> erro404(EntityNotFoundException e) {
		if (e.getMessage().contains("produto_nao_encontrado_nome")) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Não foi possível encontrar um produto com este nome. Verifique e tente novamente.");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado. Verifique e tente novamente.");
	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<String> erro404(EmptyResultDataAccessException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body("Nenhum produto foi encontrado. Verifique e tente novamente.");
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	private ResponseEntity<String> erro403e409(DataIntegrityViolationException e) {
		if (e.getMessage().contains("produto_existente")) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Produto com esse SKU já existe.");
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body("Houve um erro.");
	}

}
