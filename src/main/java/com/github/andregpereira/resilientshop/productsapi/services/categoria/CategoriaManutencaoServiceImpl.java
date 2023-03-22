package com.github.andregpereira.resilientshop.productsapi.services.categoria;

import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Categoria;
import com.github.andregpereira.resilientshop.productsapi.infra.exception.CategoriaAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.infra.exception.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.mappers.CategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.repositories.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CategoriaManutencaoServiceImpl implements CategoriaManutencaoService {

    private final CategoriaRepository repository;

    private final CategoriaMapper mapper;

    public CategoriaDto registrar(CategoriaRegistroDto dto) {
        if (repository.existsByNome(dto.nome())) {
            throw new CategoriaAlreadyExistsException("Opa! Já existe uma categoria com esse nome");
        }
        Categoria categoria = mapper.toCategoria(dto);
        return mapper.toCategoriaDto(repository.save(categoria));
    }

    public CategoriaDto atualizar(Long id, CategoriaRegistroDto dto) {
        if (!repository.existsById(id)) {
            throw new CategoriaNotFoundException(
                    "Desculpe, não foi possível encontrar uma categoria com o id " + id + ". Verifique e tente novamente");
        } else if (repository.existsByNome(dto.nome())) {
            throw new CategoriaAlreadyExistsException("Opa! Já existe uma categoria com esse nome");
        }
        Categoria categoriaAtualizada = mapper.toCategoria(dto);
        categoriaAtualizada.setId(id);
        return mapper.toCategoriaDto(repository.save(categoriaAtualizada));
    }

    public String remover(Long id) {
        repository.findById(id).ifPresentOrElse(c -> repository.deleteById(c.getId()), () -> {
            throw new CategoriaNotFoundException(
                    "Desculpe, não foi possível encontrar uma categoria com o id " + id + ". Verifique e tente novamente");
        });
        return "Categoria removida";
    }

}
