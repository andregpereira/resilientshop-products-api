package com.github.andregpereira.resilientshop.productsapi.app.services.categoria;

import com.github.andregpereira.resilientshop.productsapi.app.dto.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.CategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.CategoriaEntity;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.function.Predicate.not;

/**
 * Classe de serviço de consulta de {@link CategoriaEntity}.
 *
 * @author André Garcia
 * @see CategoriaConsultaService
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class CategoriaConsultaServiceImpl implements CategoriaConsultaService {

    /**
     * Injeção da dependência {@link CategoriaRepository} para realizar operações de
     * consulta na tabela de categorias no banco de dados.
     */
    private final CategoriaRepository repository;

    /**
     * Injeção da dependência {@link CategoriaMapper} para realizar
     * conversões de entidade para DTO de categorias.
     */
    private final CategoriaMapper mapper;

    /**
     * Lista todas as {@linkplain CategoriaEntity categorias} cadastradas.
     * Retorna uma {@linkplain Page sublista} de {@linkplain CategoriaDto categorias}.
     *
     * @param pageable o pageable padrão.
     *
     * @return uma sublista de uma lista com todas as categorias cadastradas.
     *
     * @throws CategoriaNotFoundException caso nenhuma categoria seja encontrada.
     */
    @Override
    public Page<CategoriaDto> listar(Pageable pageable) {
        return Optional.of(repository.findAll(pageable)).filter(not(Page::isEmpty)).map(p -> {
            log.info("Retornando categorias");
            return p.map(mapper::toCategoriaDto);
        }).orElseThrow(() -> {
            log.info("Não há categorias cadastradas");
            return new CategoriaNotFoundException();
        });
    }

    /**
     * Pesquisa uma {@linkplain CategoriaEntity categoria} por {@code id}.
     * Retorna uma {@linkplain  CategoriaDto categoria}.
     *
     * @param id o id da categoria.
     *
     * @return uma categoria encontrada pelo {@code id}.
     *
     * @throws CategoriaNotFoundException caso a categoria não seja encontrada.
     */
    @Override
    public CategoriaDto consultarPorId(Long id) {
        return repository.findById(id).map(c -> {
            log.info("Retornando categoria com id {}", id);
            return mapper.toCategoriaDto(c);
        }).orElseThrow(() -> {
            log.info("Categoria não encontrada com id {}", id);
            return new CategoriaNotFoundException(id);
        });
    }

}
