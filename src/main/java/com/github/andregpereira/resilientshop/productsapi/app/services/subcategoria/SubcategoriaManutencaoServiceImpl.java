package com.github.andregpereira.resilientshop.productsapi.app.services.subcategoria;

import com.github.andregpereira.resilientshop.productsapi.app.dto.subcategoria.SubcategoriaDetalhesDto;
import com.github.andregpereira.resilientshop.productsapi.app.dto.subcategoria.SubcategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.SubcategoriaAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.SubcategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.SubcategoriaEntity;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.CategoriaRepository;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.SubcategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

/**
 * Classe de serviço de manutenção de {@link SubcategoriaEntity}.
 *
 * @author André Garcia
 * @see SubcategoriaManutencaoService
 */
@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class SubcategoriaManutencaoServiceImpl implements SubcategoriaManutencaoService {

    /**
     * Injeção da dependência {@link SubcategoriaRepository} para realizar operações de
     * manutenção na tabela de subcategorias no banco de dados.
     */
    private final SubcategoriaRepository subcategoriaRepository;

    /**
     * Injeção da dependência {@link SubcategoriaMapper} para realizar
     * conversões de DTO e entidade de subcategorias.
     */
    private final SubcategoriaMapper mapper;

    /**
     * Injeção da dependência {@link CategoriaRepository} para realizar operações de
     * consulta na tabela de categorias no banco de dados.
     */
    private final CategoriaRepository categoriaRepository;

    /**
     * Cadastra uma {@linkplain SubcategoriaRegistroDto subcategoria}.
     * Retorna uma {@linkplain SubcategoriaDetalhesDto subcategoria detalhada}.
     *
     * @param dto a subcategoria a ser cadastrada.
     *
     * @return a subcategoria salva no banco de dados.
     *
     * @throws SubcategoriaAlreadyExistsException caso exista uma subcategoria com o nome já cadastrado
     * @throws CategoriaNotFoundException         caso nenhuma categoria seja encontrada.
     */
    @Override
    public SubcategoriaDetalhesDto criar(SubcategoriaRegistroDto dto) {
        if (subcategoriaRepository.existsByNome(dto.nome())) {
            log.info("Subcategoria já cadastrada com o nome {}", dto.nome());
            throw new SubcategoriaAlreadyExistsException(dto.nome());
        }
        return categoriaRepository.findById(dto.idCategoria()).map(c -> {
            SubcategoriaEntity subcategoria = mapper.toSubcategoria(dto);
            subcategoria.setCategoria(c);
            subcategoriaRepository.save(subcategoria);
            log.info("Subcategoria criada");
            return mapper.toSubcategoriaDetalhesDto(subcategoria);
        }).orElseThrow(() -> {
            log.info("Categoria não encontrada com id {}", dto.idCategoria());
            return new CategoriaNotFoundException(dto.idCategoria());
        });
    }

    /**
     * Atualiza uma {@linkplain SubcategoriaRegistroDto subcategoria} por {@code id}.
     * Retorna uma {@linkplain SubcategoriaDetalhesDto subcategoria detalhada}.
     *
     * @param id  o id da subcategoria a ser atualizada.
     * @param dto a subcategoria a ser atualizada.
     *
     * @return a subcategoria atualizada.
     *
     * @throws SubcategoriaNotFoundException      caso nenhuma subcategoria seja encontrada.
     * @throws SubcategoriaAlreadyExistsException caso exista uma subcategoria com o nome já cadastrado
     * @throws CategoriaNotFoundException         caso nenhuma categoria seja encontrada.
     */
    @Override
    public SubcategoriaDetalhesDto atualizar(Long id, SubcategoriaRegistroDto dto) {
        return subcategoriaRepository.findById(id).map(subcategoriaAntiga -> {
            if (subcategoriaRepository.existsByNome(dto.nome())) {
                log.info("Subcategoria já cadastrada com o nome {}", dto.nome());
                throw new SubcategoriaAlreadyExistsException(dto.nome());
            }
            return categoriaRepository.findById(dto.idCategoria()).map(c -> {
                SubcategoriaEntity subcategoriaAtualizada = mapper.toSubcategoria(dto);
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

    /**
     * Remove uma {@linkplain SubcategoriaEntity subcategoria} por {@code id}.
     * Retorna uma mensagem de confirmação de remoção.
     *
     * @param id o id da subcategoria a ser removida.
     *
     * @return uma mensagem de confirmação de remoção.
     *
     * @throws SubcategoriaNotFoundException caso nenhuma subcategoria seja encontrada.
     */
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
