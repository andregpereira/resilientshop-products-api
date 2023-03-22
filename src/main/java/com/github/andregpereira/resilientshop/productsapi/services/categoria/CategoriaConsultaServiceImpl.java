package com.github.andregpereira.resilientshop.productsapi.services.categoria;

import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Categoria;
import com.github.andregpereira.resilientshop.productsapi.infra.exception.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.mappers.CategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.repositories.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoriaConsultaServiceImpl implements CategoriaConsultaService {

    private final CategoriaRepository repository;

    private final CategoriaMapper mapper;

    public Page<CategoriaDto> listar(Pageable pageable) {
        Page<Categoria> categorias = repository.findAll(pageable);
        if (categorias.isEmpty())
            throw new CategoriaNotFoundException("Ops! Ainda não há categorias cadastradas");
        return categorias.map(c -> mapper.toCategoriaDto(c));
    }

    public CategoriaDto consultarPorId(Long id) {
        if (!repository.existsById(id)) {
            throw new CategoriaNotFoundException(
                    "Desculpe, não foi possível encontrar uma categoria com o id " + id + ". Verifique e tente novamente");
        }
        return mapper.toCategoriaDto(repository.getReferenceById(id));
    }

}
