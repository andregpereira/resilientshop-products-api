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

    @Override
    public SubcategoriaDetalhesDto criar(SubcategoriaRegistroDto dto) {
        if (subcategoriaRepository.existsByNome(dto.nome())) {
            log.info("Subcategoria já cadastrada com o nome {}", dto.nome());
            throw new SubcategoriaAlreadyExistsException(dto.nome());
        }
        return categoriaRepository.findById(dto.idCategoria()).map(c -> {
            Subcategoria subcategoria = mapper.toSubcategoria(dto);
            subcategoria.setCategoria(c);
            subcategoriaRepository.save(subcategoria);
            log.info("Subcategoria criada");
            return mapper.toSubcategoriaDetalhesDto(subcategoria);
        }).orElseThrow(() -> {
            log.info("Categoria não encontrada com id {}", dto.idCategoria());
            return new CategoriaNotFoundException(dto.idCategoria());
        });
    }

    @Override
    public SubcategoriaDetalhesDto atualizar(Long id, SubcategoriaRegistroDto dto) {
        return subcategoriaRepository.findById(id).map(subcategoriaAntiga -> {
            if (subcategoriaRepository.existsByNome(dto.nome())) {
                log.info("Subcategoria já cadastrada com o nome {}", dto.nome());
                throw new SubcategoriaAlreadyExistsException(dto.nome());
            }
            return categoriaRepository.findById(dto.idCategoria()).map(c -> {
                Subcategoria subcategoriaAtualizada = mapper.toSubcategoria(dto);
                subcategoriaAtualizada.setId(id);
                subcategoriaAtualizada.setCategoria(c);
                subcategoriaRepository.save(subcategoriaAtualizada);
                log.info("Subcategoria com id {} atualizada", id);
                return mapper.toSubcategoriaDetalhesDto(subcategoriaAtualizada);
            }).orElseThrow(() -> {
                log.info("Categoria não encontrada com id {}", dto.idCategoria());
                return new CategoriaNotFoundException(dto.idCategoria());
            });
        }).orElseThrow(() -> {
            log.info("Subcategoria não encontrada com id {}", id);
            return new SubcategoriaNotFoundException(id);
        });
    }

    @Override
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
