package com.github.andregpereira.resilientshop.productsapi.services.subcategoria;

import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria.SubcategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.entities.Subcategoria;
import com.github.andregpereira.resilientshop.productsapi.infra.exception.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.infra.exception.SubcategoriaAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.infra.exception.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.mappers.SubcategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.repositories.CategoriaRepository;
import com.github.andregpereira.resilientshop.productsapi.repositories.SubcategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class SubcategoriaManutencaoServiceImpl implements SubcategoriaManutencaoService {

    private final SubcategoriaRepository subcategoriaRepository;

    private final SubcategoriaMapper mapper;

    private final CategoriaRepository categoriaRepository;

    public SubcategoriaDetalhesDto registrar(SubcategoriaRegistroDto dto) {
        if (!categoriaRepository.existsById(dto.idCategoria())) {
            throw new CategoriaNotFoundException(
                    "Desculpe, não foi possível encontrar uma categoria com o id " + dto.idCategoria() + ". Verifique e tente novamente");
        } else if (subcategoriaRepository.existsByNome(dto.nome())) {
            throw new SubcategoriaAlreadyExistsException("Opa! Já existe uma subcategoria com esse nome");
        }
        Subcategoria subcategoria = mapper.toSubcategoria(dto);
        subcategoria.setCategoria(categoriaRepository.getReferenceById(dto.idCategoria()));
        return mapper.toSubcategoriaDetalhesDto(subcategoriaRepository.save(subcategoria));
    }

    public SubcategoriaDetalhesDto atualizar(Long id, SubcategoriaRegistroDto dto) {
        if (!subcategoriaRepository.existsById(id)) {
            throw new SubcategoriaNotFoundException(
                    "Desculpe, não foi possível encontrar uma subcategoria com o id " + id + ". Verifique e tente novamente");
        } else if (!categoriaRepository.existsById(dto.idCategoria())) {
            throw new CategoriaNotFoundException(
                    "Desculpe, não foi possível encontrar uma categoria com o id " + dto.idCategoria() + ". Verifique e tente novamente");
        } else if (subcategoriaRepository.existsByNome(dto.nome())) {
            throw new SubcategoriaAlreadyExistsException("Opa! Já existe uma subcategoria com esse nome");
        }
        Subcategoria subcategoriaAtualizada = mapper.toSubcategoria(dto);
        subcategoriaAtualizada.setId(id);
        subcategoriaAtualizada.setCategoria(categoriaRepository.getReferenceById(dto.idCategoria()));
        return mapper.toSubcategoriaDetalhesDto(subcategoriaRepository.save(subcategoriaAtualizada));
    }

    public String remover(Long id) {
        subcategoriaRepository.findById(id).ifPresentOrElse(c -> subcategoriaRepository.deleteById(c.getId()), () -> {
            throw new SubcategoriaNotFoundException(
                    "Desculpe, não foi possível encontrar uma subcategoria com o id " + id + ". Verifique e tente novamente");
        });
        return "Subcategoria removida";
    }

}
