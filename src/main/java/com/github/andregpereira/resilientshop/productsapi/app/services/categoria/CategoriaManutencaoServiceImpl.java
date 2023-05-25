package com.github.andregpereira.resilientshop.productsapi.app.services.categoria;

import com.github.andregpereira.resilientshop.productsapi.app.dtos.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.categoria.CategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.CategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.Categoria;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class CategoriaManutencaoServiceImpl implements CategoriaManutencaoService {

    private final CategoriaRepository repository;
    private final CategoriaMapper mapper;

    @Override
    public CategoriaDto criar(CategoriaRegistroDto dto) {
        if (repository.existsByNome(dto.nome())) {
            log.info("Categoria já cadastrada com o nome {}", dto.nome());
            throw new CategoriaAlreadyExistsException(dto.nome());
        }
        Categoria categoria = mapper.toCategoria(dto);
        repository.save(categoria);
        log.info("Categoria criada");
        return mapper.toCategoriaDto(categoria);
    }

    @Override
    public CategoriaDto atualizar(Long id, CategoriaRegistroDto dto) {
        return repository.findById(id).map(categoriaAntiga -> {
            if (repository.existsByNome(dto.nome())) {
                log.info("Categoria já cadastrada com o nome {}", dto.nome());
                throw new CategoriaAlreadyExistsException(dto.nome());
            }
            Categoria categoriaAtualizada = mapper.toCategoria(dto);
            categoriaAtualizada.setId(id);
            repository.save(categoriaAtualizada);
            log.info("Categoria com id {} atualizada", id);
            return mapper.toCategoriaDto(categoriaAtualizada);
        }).orElseThrow(() -> {
            log.info("Categoria não encontrada com id {}", id);
            return new CategoriaNotFoundException(id);
        });
    }

    @Override
    public String remover(Long id) {
        return repository.findById(id).map(c -> {
            repository.deleteById(id);
            log.info("Categoria com id {} removida", id);
            return MessageFormat.format("Categoria com id {0} removida com sucesso", id);
        }).orElseThrow(() -> {
            log.info("Categoria não encontrada com id {}", id);
            return new CategoriaNotFoundException(id);
        });
    }

}
