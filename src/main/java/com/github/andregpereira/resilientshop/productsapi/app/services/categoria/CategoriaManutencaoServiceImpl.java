package com.github.andregpereira.resilientshop.productsapi.app.services.categoria;

import com.github.andregpereira.resilientshop.productsapi.app.dto.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.categoria.CategoriaRegistroDto;
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

/**
 * Classe de serviço de manutenção de {@link Categoria}.
 *
 * @author André Garcia
 * @see CategoriaManutencaoService
 */
@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class CategoriaManutencaoServiceImpl implements CategoriaManutencaoService {

    /**
     * Injeção da dependência {@link CategoriaRepository} para realizar operações de
     * manutenção na tabela de categorias no banco de dados.
     */
    private final CategoriaRepository repository;

    /**
     * Injeção da dependência {@link CategoriaMapper} para realizar
     * conversões de DTO e entidade de categorias.
     */
    private final CategoriaMapper mapper;

    /**
     * Cadastra uma {@linkplain CategoriaRegistroDto categoria}.
     * Retorna uma {@linkplain CategoriaDto categoria}.
     *
     * @param dto a categoria a ser cadastrada.
     *
     * @return a categoria salva no banco de dados.
     *
     * @throws CategoriaAlreadyExistsException caso exista uma categoria com o nome já cadastrado
     * @throws CategoriaNotFoundException      caso nenhuma categoria seja encontrada.
     */
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

    /**
     * Atualiza uma {@linkplain CategoriaRegistroDto categoria} por {@code id}.
     * Retorna uma {@linkplain CategoriaDto categoria}.
     *
     * @param id  o id da categoria a ser atualizada.
     * @param dto a categoria a ser atualizada.
     *
     * @return a categoria atualizada.
     *
     * @throws CategoriaNotFoundException      caso nenhuma categoria seja encontrada.
     * @throws CategoriaAlreadyExistsException caso exista uma categoria com o nome já cadastrado
     * @throws CategoriaNotFoundException      caso nenhuma categoria seja encontrada.
     */
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

    /**
     * Remove uma {@linkplain Categoria categoria} por {@code id}.
     * Retorna uma mensagem de confirmação de remoção.
     *
     * @param id o id da categoria a ser removida.
     *
     * @return uma mensagem de confirmação de remoção.
     *
     * @throws CategoriaNotFoundException caso nenhuma categoria seja encontrada.
     */
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
