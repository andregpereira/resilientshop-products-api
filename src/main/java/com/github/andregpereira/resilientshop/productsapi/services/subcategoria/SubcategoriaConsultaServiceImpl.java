package com.github.andregpereira.resilientshop.productsapi.services.subcategoria;

import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Subcategoria;
import com.github.andregpereira.resilientshop.productsapi.infra.exception.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.mappers.SubcategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.repositories.SubcategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubcategoriaConsultaServiceImpl implements SubcategoriaConsultaService {

    private final SubcategoriaRepository repository;

    private final SubcategoriaMapper mapper;

    public Page<SubcategoriaDto> listar(Pageable pageable) {
        Page<Subcategoria> subcategorias = repository.findAll(pageable);
        if (subcategorias.isEmpty())
            throw new SubcategoriaNotFoundException("Ops! Ainda não há subcategorias cadastradas");
        return subcategorias.map(sc -> mapper.toSubcategoriaDto(sc));
    }

    public SubcategoriaDetalhesDto consultarPorId(Long id) {
        if (!repository.existsById(id)) {
            throw new SubcategoriaNotFoundException(
                    "Desculpe, não foi possível encontrar uma subcategoria com o id " + id + ". Verifique e tente novamente");
        }
        return mapper.toSubcategoriaDetalhesDto(repository.getReferenceById(id));
    }

}
