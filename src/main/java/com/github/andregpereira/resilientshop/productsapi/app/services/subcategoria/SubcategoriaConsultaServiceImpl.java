package com.github.andregpereira.resilientshop.productsapi.app.services.subcategoria;

import com.github.andregpereira.resilientshop.productsapi.app.dtos.subcategoria.SubcategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.subcategoria.SubcategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.SubcategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.Subcategoria;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.SubcategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class SubcategoriaConsultaServiceImpl implements SubcategoriaConsultaService {

    private final SubcategoriaRepository repository;
    private final SubcategoriaMapper mapper;

    @Override
    public Page<SubcategoriaDto> listar(Pageable pageable) {
        Page<Subcategoria> subcategorias = repository.findAll(pageable);
        if (subcategorias.isEmpty()) {
            log.info("Não há subcategorias cadastradas");
            throw new SubcategoriaNotFoundException();
        }
        log.info("Retornando subcategorias");
        return subcategorias.map(mapper::toSubcategoriaDto);
    }

    @Override
    public SubcategoriaDetalhesDto consultarPorId(Long id) {
        return repository.findById(id).map(c -> {
            log.info("Retornando subcategoria com id {}", id);
            return mapper.toSubcategoriaDetalhesDto(c);
        }).orElseThrow(() -> {
            log.info("Subcategoria não encontrada com id {}", id);
            return new SubcategoriaNotFoundException(id);
        });
    }

}
