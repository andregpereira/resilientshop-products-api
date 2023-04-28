package com.github.andregpereira.resilientshop.productsapi.app.services.subcategoria;

import com.github.andregpereira.resilientshop.productsapi.app.dtos.subcategoria.SubcategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dtos.subcategoria.SubcategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.SubcategoriaAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.SubcategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.Subcategoria;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.CategoriaRepository;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.SubcategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class SubcategoriaManutencaoServiceImpl implements SubcategoriaManutencaoService {

    private final SubcategoriaRepository subcategoriaRepository;
    private final SubcategoriaMapper mapper;
    private final CategoriaRepository categoriaRepository;

    public SubcategoriaDetalhesDto registrar(SubcategoriaRegistroDto dto) {
        if (!categoriaRepository.existsById(dto.idCategoria())) {
            log.info("Categoria não encontrada com id {}", dto.idCategoria());
            throw new CategoriaNotFoundException(dto.idCategoria());
        } else if (subcategoriaRepository.existsByNome(dto.nome())) {
            log.info("Subcategoria já cadastrada com o nome {}", dto.nome());
            throw new SubcategoriaAlreadyExistsException(dto.nome());
        }
        Subcategoria subcategoria = mapper.toSubcategoria(dto);
        subcategoria.setCategoria(categoriaRepository.getReferenceById(dto.idCategoria()));
        subcategoria = subcategoriaRepository.save(subcategoria);
        log.info("Subcategoria criada");
        return mapper.toSubcategoriaDetalhesDto(subcategoria);
    }

    public SubcategoriaDetalhesDto atualizar(Long id, SubcategoriaRegistroDto dto) {
        if (!subcategoriaRepository.existsById(id)) {
            log.info("Subcategoria não ecnontrada com id {}", id);
            throw new SubcategoriaNotFoundException(id);
        } else if (!categoriaRepository.existsById(dto.idCategoria())) {
            log.info("Categoria não encontrada com id {}", dto.idCategoria());
            throw new CategoriaNotFoundException(dto.idCategoria());
        } else if (subcategoriaRepository.existsByNome(dto.nome())) {
            log.info("Subcategoria já cadastrada com o nome {}", dto.nome());
            throw new SubcategoriaAlreadyExistsException(dto.nome());
        }
        Subcategoria subcategoriaAtualizada = mapper.toSubcategoria(dto);
        subcategoriaAtualizada.setId(id);
        subcategoriaAtualizada.setCategoria(categoriaRepository.getReferenceById(dto.idCategoria()));
        Subcategoria subcategoria = subcategoriaRepository.save(subcategoriaAtualizada);
        log.info("Subcategoria com id {} atualizada", id);
        return mapper.toSubcategoriaDetalhesDto(subcategoria);
    }

    public String remover(Long id) {
        return subcategoriaRepository.findById(id).map(c -> {
            subcategoriaRepository.deleteById(id);
            log.info("Subcategoria com id {} removida", id);
            return MessageFormat.format("Subcategoria com id {0} removida com sucesso", id);
        }).orElseThrow(() -> {
            log.info("Subcategoria não encontrada com id {}", id);
            return new SubcategoriaNotFoundException(id);
        });
    }

}
