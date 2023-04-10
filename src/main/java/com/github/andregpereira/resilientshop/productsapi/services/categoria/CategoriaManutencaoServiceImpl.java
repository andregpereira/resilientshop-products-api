package com.github.andregpereira.resilientshop.productsapi.services.categoria;

import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.categoria.CategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Categoria;
import com.github.andregpereira.resilientshop.productsapi.infra.exception.CategoriaAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.infra.exception.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.mappers.CategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.repositories.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class CategoriaManutencaoServiceImpl implements CategoriaManutencaoService {

    private final CategoriaRepository repository;

    private final CategoriaMapper mapper;

    public CategoriaDto registrar(CategoriaRegistroDto dto) {
        if (repository.existsByNome(dto.nome())) {
            log.info("Categoria já cadastrada com o nome {}", dto.nome());
            throw new CategoriaAlreadyExistsException("Opa! Já existe uma categoria com esse nome");
        }
        Categoria categoria = mapper.toCategoria(dto);
        categoria = repository.save(categoria);
        log.info("Categoria criada");
        return mapper.toCategoriaDto(categoria);
    }

    public CategoriaDto atualizar(Long id, CategoriaRegistroDto dto) {
        if (!repository.existsById(id)) {
            log.info("Categoria não encontrada com id {}", id);
            throw new CategoriaNotFoundException(
                    "Desculpe, não foi possível encontrar uma categoria com o id " + id + ". Verifique e tente novamente");
        } else if (repository.existsByNome(dto.nome())) {
            log.info("Categoria já cadastrada com o nome {}", dto.nome());
            throw new CategoriaAlreadyExistsException("Opa! Já existe uma categoria com esse nome");
        }
        Categoria categoriaAtualizada = mapper.toCategoria(dto);
        categoriaAtualizada.setId(id);
        Categoria categoria = repository.save(categoriaAtualizada);
        log.info("Categoria com id {} atualizada", id);
        return mapper.toCategoriaDto(categoria);
    }

    public String remover(Long id) {
        repository.findById(id).ifPresentOrElse(c -> repository.deleteById(id), () -> {
            log.info("Categoria não encontrada com id {}", id);
            throw new CategoriaNotFoundException(
                    "Desculpe, não foi possível encontrar uma categoria com o id " + id + ". Verifique e tente novamente");
        });
        log.info("Categoria com id {} removida", id);
        return "Categoria removida";
    }

}
