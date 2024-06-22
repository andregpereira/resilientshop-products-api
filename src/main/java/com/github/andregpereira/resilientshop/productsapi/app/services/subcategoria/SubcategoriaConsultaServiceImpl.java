package com.github.andregpereira.resilientshop.productsapi.app.services.subcategoria;

import com.github.andregpereira.resilientshop.productsapi.app.dto.subcategoria.SubcategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.subcategoria.SubcategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.SubcategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.SubcategoriaEntity;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.SubcategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Classe de serviço de consulta de {@link SubcategoriaEntity}.
 *
 * @author André Garcia
 * @see SubcategoriaConsultaService
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class SubcategoriaConsultaServiceImpl implements SubcategoriaConsultaService {

    /**
     * Injeção da dependência {@link SubcategoriaRepository} para realizar operações de
     * consulta na tabela de subcategorias no banco de dados.
     */
    private final SubcategoriaRepository repository;

    /**
     * Injeção da dependência {@link SubcategoriaMapper} para realizar
     * conversões de entidade para DTO de subcategorias.
     */
    private final SubcategoriaMapper mapper;

    /**
     * Lista todas as {@linkplain SubcategoriaEntity subcategorias} cadastradas.
     * Retorna uma {@linkplain Page sublista} de {@linkplain SubcategoriaDto subcategorias}.
     *
     * @param pageable o pageable padrão.
     *
     * @return uma sublista de uma lista com todas as subcategorias cadastradas.
     *
     * @throws SubcategoriaNotFoundException caso nenhuma subcategoria seja encontrada.
     */
    @Override
    public Page<SubcategoriaDto> listar(Pageable pageable) {
        log.info("Retornando subcategorias");
        return repository.findAll(pageable).map(mapper::toSubcategoriaDto);
    }

    /**
     * Pesquisa uma {@linkplain SubcategoriaEntity subcategoria} por {@code id}.
     * Retorna uma {@linkplain  SubcategoriaDetalhesDto subcategoria detalhada}.
     *
     * @param id o id da subcategoria.
     *
     * @return uma subcategoria encontrada pelo {@code id}.
     *
     * @throws SubcategoriaNotFoundException caso a subcategoria não seja encontrada.
     */
    @Override
    public SubcategoriaDetalhesDto consultarPorId(Long id) {
        log.info("Retornando subcategoria com id {}", id);
        return repository.findById(id).map(mapper::toSubcategoriaDetalhesDto).orElseThrow(() -> {
            log.info("Subcategoria não encontrada com id {}", id);
            return new SubcategoriaNotFoundException(id);
        });
    }

}
