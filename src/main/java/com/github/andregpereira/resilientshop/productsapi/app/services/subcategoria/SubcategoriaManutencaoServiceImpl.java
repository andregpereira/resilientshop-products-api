package com.github.andregpereira.resilientshop.productsapi.services.subcategoria;

import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Subcategoria;
import com.github.andregpereira.resilientshop.productsapi.infra.exceptions.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.infra.exceptions.SubcategoriaAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.infra.exceptions.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.mappers.SubcategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.repositories.CategoriaRepository;
import com.github.andregpereira.resilientshop.productsapi.repositories.SubcategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new CategoriaNotFoundException(
                    "Desculpe, não foi possível encontrar uma categoria com o id " + dto.idCategoria() + ". Verifique e tente novamente");
        } else if (subcategoriaRepository.existsByNome(dto.nome())) {
            log.info("Subcategoria já cadastrada com o nome {}", dto.nome());
            throw new SubcategoriaAlreadyExistsException("Opa! Já existe uma subcategoria com esse nome");
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
            throw new SubcategoriaNotFoundException(
                    "Desculpe, não foi possível encontrar uma subcategoria com o id " + id + ". Verifique e tente novamente");
        } else if (!categoriaRepository.existsById(dto.idCategoria())) {
            log.info("Categoria não encontrada com id {}", dto.idCategoria());
            throw new CategoriaNotFoundException(
                    "Desculpe, não foi possível encontrar uma categoria com o id " + dto.idCategoria() + ". Verifique e tente novamente");
        } else if (subcategoriaRepository.existsByNome(dto.nome())) {
            log.info("Subcategoria já cadastrada com o nome {}", dto.nome());
            throw new SubcategoriaAlreadyExistsException("Opa! Já existe uma subcategoria com esse nome");
        }
        Subcategoria subcategoriaAtualizada = mapper.toSubcategoria(dto);
        subcategoriaAtualizada.setId(id);
        subcategoriaAtualizada.setCategoria(categoriaRepository.getReferenceById(dto.idCategoria()));
        Subcategoria subcategoria = subcategoriaRepository.save(subcategoriaAtualizada);
        log.info("Subcategoria com id {} atualizada", id);
        return mapper.toSubcategoriaDetalhesDto(subcategoria);
    }

    public String remover(Long id) {
        subcategoriaRepository.findById(id).ifPresentOrElse(c -> subcategoriaRepository.deleteById(id), () -> {
            log.info("Subcategoria não ecnontrada com id {}", id);
            throw new SubcategoriaNotFoundException(
                    "Desculpe, não foi possível encontrar uma subcategoria com o id " + id + ". Verifique e tente novamente");
        });
        log.info("Subcategoria com id {} removida", id);
        return "Subcategoria removida";
    }

}
