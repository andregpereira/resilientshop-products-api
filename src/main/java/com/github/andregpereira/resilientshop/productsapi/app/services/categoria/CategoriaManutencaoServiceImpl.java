package com.github.andregpereira.resilientshop.productsapi.app.services.categoria;

import com.github.andregpereira.resilientshop.productsapi.app.dto.categoria.CategoriaDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.categoria.CategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.CategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.CategoriaEntity;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 * Classe de serviço de manutenção de {@link CategoriaEntity}.
 *
 * @author André Garcia
 * @see CategoriaManutencaoService
 */
@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class CategoriaManutencaoServiceImpl implements CategoriaManutencaoService {

    private static final Consumer<String> CATEGORIA_EXISTENTE_LOG =
        nome -> log.info("Categoria já cadastrada com o nome {}", nome);

    private static final LongConsumer CATEGORIA_NAO_ENCONTRADA_LOG =
        id -> log.info("Categoria não encontrada com id {}", id);

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
            CATEGORIA_EXISTENTE_LOG.accept(dto.nome());
            throw new CategoriaAlreadyExistsException(dto.nome());
        }
        CategoriaEntity categoria = mapper.toCategoria(dto);
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
        return repository
            .findById(id)
            .map(categoriaAntiga -> {
                if (repository.existsByNome(dto.nome())) {
                    CATEGORIA_EXISTENTE_LOG.accept(dto.nome());
                    throw new CategoriaAlreadyExistsException(dto.nome());
                }
                CategoriaEntity categoriaAtualizada = mapper.toCategoria(dto);
                categoriaAtualizada.setId(id);
                repository.save(categoriaAtualizada);
                log.info("Categoria com id {} atualizada", id);
                return mapper.toCategoriaDto(categoriaAtualizada);
            })
            .orElseThrow(() -> {
                CATEGORIA_NAO_ENCONTRADA_LOG.accept(id);
                return new CategoriaNotFoundException(id);
            });
    }

    /**
     * Remove uma {@linkplain CategoriaEntity categoria} por {@code id}.
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
        return repository
            .findById(id)
            .map(c -> {
                repository.deleteById(id);
                log.info("Categoria com id {} removida", id);
                return MessageFormat.format("Categoria com id {0} removida com sucesso", id);
            })
            .orElseThrow(() -> {
                CATEGORIA_NAO_ENCONTRADA_LOG.accept(id);
                return new CategoriaNotFoundException(id);
            });
    }

}
