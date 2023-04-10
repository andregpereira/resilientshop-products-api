package com.github.andregpereira.resilientshop.productsapi.services.subcategoria;

import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Subcategoria;
import com.github.andregpereira.resilientshop.productsapi.infra.exception.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.mappers.SubcategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.repositories.SubcategoriaRepository;
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

    public Page<SubcategoriaDto> listar(Pageable pageable) {
        Page<Subcategoria> subcategorias = repository.findAll(pageable);
        if (subcategorias.isEmpty()) {
            log.info("Não há subcategorias cadastradas");
            throw new SubcategoriaNotFoundException("Ops! Ainda não há subcategorias cadastradas");
        }
        log.info("Retornando subcategorias");
        return subcategorias.map(mapper::toSubcategoriaDto);
    }

    public SubcategoriaDetalhesDto consultarPorId(Long id) {
        if (!repository.existsById(id)) {
            log.info("Subcategoria não encontrada com id {}", id);
            throw new SubcategoriaNotFoundException(
                    "Desculpe, não foi possível encontrar uma subcategoria com o id " + id + ". Verifique e tente novamente");
        }
        log.info("Retornando subcategoria com id {}", id);
        return mapper.toSubcategoriaDetalhesDto(repository.getReferenceById(id));
    }

}
