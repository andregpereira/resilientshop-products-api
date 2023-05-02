package com.github.andregpereira.resilientshop.productsapi.app.services.categoria;

import com.github.andregpereira.resilientshop.productsapi.app.dtos.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.CategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.Categoria;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class CategoriaConsultaServiceImpl implements CategoriaConsultaService {

    private final CategoriaRepository repository;
    private final CategoriaMapper mapper;

    public Page<CategoriaDto> listar(Pageable pageable) {
        Page<Categoria> categorias = repository.findAll(pageable);
        if (categorias.isEmpty()) {
            log.info("Não há categorias cadastradas");
            throw new CategoriaNotFoundException();
        }
        log.info("Retornando categorias");
        return categorias.map(mapper::toCategoriaDto);
    }

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
