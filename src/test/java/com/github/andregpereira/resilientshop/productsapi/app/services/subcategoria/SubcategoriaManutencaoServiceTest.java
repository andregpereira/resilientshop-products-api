package com.github.andregpereira.resilientshop.productsapi.app.services.subcategoria;

import com.github.andregpereira.resilientshop.productsapi.app.dto.subcategoria.SubcategoriaRegistroDto;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.CategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.SubcategoriaAlreadyExistsException;
import com.github.andregpereira.resilientshop.productsapi.cross.exceptions.SubcategoriaNotFoundException;
import com.github.andregpereira.resilientshop.productsapi.cross.mappers.SubcategoriaMapper;
import com.github.andregpereira.resilientshop.productsapi.infra.entities.SubcategoriaEntity;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.CategoriaRepository;
import com.github.andregpereira.resilientshop.productsapi.infra.repositories.SubcategoriaRepository;
import org.assertj.core.api.BDDAssertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.MessageFormat;
import java.util.Optional;

import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.CategoriaMockFactory.getCategoriaEntity;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.SubcategoriaMockFactory.getSubcategoriaDetalhesDto;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.SubcategoriaMockFactory.getSubcategoriaEntity;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.SubcategoriaMockFactory.getSubcategoriaInvalida;
import static com.github.andregpereira.resilientshop.productsapi.util.mock.factory.SubcategoriaMockFactory.getSubcategoriaRegistroDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class SubcategoriaManutencaoServiceTest {

    @InjectMocks
    private SubcategoriaManutencaoServiceImpl manutencaoService;

    @Mock
    private SubcategoriaMapper mapper;

    @Mock
    private SubcategoriaRepository subcategoriaRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Test
    void criarSubcategoriaComDadosValidosRetornaSubcategoriaDetalhesDto() {
        given(subcategoriaRepository.existsByNome(anyString())).willReturn(false);
        given(categoriaRepository.findById(anyLong())).willReturn(Optional.of(getCategoriaEntity()));
        given(mapper.toSubcategoria(any(SubcategoriaRegistroDto.class))).willReturn(getSubcategoriaEntity());
        given(subcategoriaRepository.save(any(SubcategoriaEntity.class))).willReturn(getSubcategoriaEntity());
        given(mapper.toSubcategoriaDetalhesDto(any(SubcategoriaEntity.class))).willReturn(getSubcategoriaDetalhesDto());

        final var sut = manutencaoService.criar(getSubcategoriaRegistroDto());

        BDDAssertions
            .then(sut)
            .isNotNull()
            .isEqualTo(getSubcategoriaDetalhesDto());
        then(subcategoriaRepository)
            .should()
            .existsByNome(anyString());
        then(categoriaRepository)
            .should()
            .findById(anyLong());
        then(mapper)
            .should()
            .toSubcategoria(any(SubcategoriaRegistroDto.class));
        then(subcategoriaRepository)
            .should()
            .save(any(SubcategoriaEntity.class));
        then(mapper)
            .should()
            .toSubcategoriaDetalhesDto(any(SubcategoriaEntity.class));
    }

    @Test
    void criarSubcategoriaComDadosInvalidosThrowsException() {
        final ThrowingCallable sut = () -> manutencaoService.criar((SubcategoriaRegistroDto) getSubcategoriaInvalida());

        assertThatThrownBy(sut).isInstanceOf(RuntimeException.class);
        then(subcategoriaRepository)
            .should(never())
            .save(any(SubcategoriaEntity.class));
        then(mapper)
            .should(never())
            .toSubcategoriaDetalhesDto(any(SubcategoriaEntity.class));
    }

    @Test
    void criarSubcategoriaComNomeExistenteThrowsException() {
        final var dto = getSubcategoriaRegistroDto();
        given(subcategoriaRepository.existsByNome(anyString())).willReturn(true);

        final ThrowingCallable sut = () -> manutencaoService.criar(dto);

        assertThatThrownBy(sut)
            .isInstanceOf(SubcategoriaAlreadyExistsException.class)
            .hasMessage(MessageFormat.format("Poxa! Já existe uma subcategoria cadastrada com o nome {0}", dto.nome()));
        then(subcategoriaRepository)
            .should(never())
            .save(any(SubcategoriaEntity.class));
        then(subcategoriaRepository).shouldHaveNoMoreInteractions();
        then(mapper).shouldHaveNoInteractions();
    }

    @Test
    void criarSubcategoriaComIdCategoriaInexistenteThrowsException() {
        given(subcategoriaRepository.existsByNome(anyString())).willReturn(false);
        given(categoriaRepository.findById(anyLong())).willReturn(Optional.empty());

        final ThrowingCallable sut = () -> manutencaoService.criar(getSubcategoriaRegistroDto());

        assertThatThrownBy(sut)
            .isInstanceOf(CategoriaNotFoundException.class)
            .hasMessage("Ops! Nenhuma categoria foi encontrada com o id 1");
        then(subcategoriaRepository)
            .should(never())
            .save(any(SubcategoriaEntity.class));
        then(categoriaRepository)
            .should()
            .findById(anyLong());
        then(subcategoriaRepository).shouldHaveNoMoreInteractions();
        then(mapper).shouldHaveNoInteractions();
    }

    @Test
    void atualizarSubcategoriaComDadosValidosRetornaSubcategoriaDetalhesDto() {
        given(subcategoriaRepository.findById(anyLong())).willReturn(Optional.of(getSubcategoriaEntity()));
        given(subcategoriaRepository.existsByNome(anyString())).willReturn(false);
        given(categoriaRepository.findById(anyLong())).willReturn(Optional.of(getCategoriaEntity()));
        given(mapper.toSubcategoria(any(SubcategoriaRegistroDto.class))).willReturn(getSubcategoriaEntity());
        given(subcategoriaRepository.save(any(SubcategoriaEntity.class))).willReturn(getSubcategoriaEntity());
        given(mapper.toSubcategoriaDetalhesDto(any(SubcategoriaEntity.class))).willReturn(getSubcategoriaDetalhesDto());

        final var sut = manutencaoService.atualizar(1L, getSubcategoriaRegistroDto());

        BDDAssertions
            .then(sut)
            .isEqualTo(getSubcategoriaDetalhesDto());
        then(subcategoriaRepository)
            .should()
            .findById(anyLong());
        then(subcategoriaRepository)
            .should()
            .existsByNome(anyString());
        then(categoriaRepository)
            .should()
            .findById(anyLong());
        then(mapper)
            .should()
            .toSubcategoria(any(SubcategoriaRegistroDto.class));
        then(subcategoriaRepository)
            .should()
            .save(any(SubcategoriaEntity.class));
        then(mapper)
            .should()
            .toSubcategoriaDetalhesDto(any(SubcategoriaEntity.class));
    }

    @Test
    void atualizarSubcategoriaComDadosInvalidosThrowsException() {
        final ThrowingCallable sut = () -> manutencaoService.atualizar(10L, getSubcategoriaRegistroDto());

        assertThatThrownBy(sut).isInstanceOf(RuntimeException.class);
        then(subcategoriaRepository)
            .should(never())
            .save(any(SubcategoriaEntity.class));
    }

    @Test
    void atualizarSubcategoriaComIdInexistenteThrowsException() {
        given(subcategoriaRepository.findById(anyLong())).willReturn(Optional.empty());

        final ThrowingCallable sut = () -> manutencaoService.atualizar(10L, getSubcategoriaRegistroDto());

        assertThatThrownBy(sut)
            .isInstanceOf(SubcategoriaNotFoundException.class)
            .hasMessage("Ops! Nenhuma subcategoria foi encontrada com o id 10");
        then(subcategoriaRepository)
            .should()
            .findById(anyLong());
        then(subcategoriaRepository).shouldHaveNoMoreInteractions();
        then(categoriaRepository).shouldHaveNoInteractions();
        then(mapper).shouldHaveNoInteractions();
    }

    @Test
    void atualizarSubcategoriaComNomeExistenteThrowsException() {
        final var dto = getSubcategoriaRegistroDto();
        given(subcategoriaRepository.findById(anyLong())).willReturn(Optional.of(getSubcategoriaEntity()));
        given(subcategoriaRepository.existsByNome(anyString())).willReturn(true);

        final ThrowingCallable sut = () -> manutencaoService.atualizar(1L, dto);

        assertThatThrownBy(sut)
            .isInstanceOf(SubcategoriaAlreadyExistsException.class)
            .hasMessage(MessageFormat.format(
                "Poxa! Já existe uma subcategoria cadastrada com o nome {0}",
                dto.nome()
            ));
        then(subcategoriaRepository)
            .should()
            .findById(anyLong());
        then(subcategoriaRepository)
            .should()
            .existsByNome(anyString());
        then(subcategoriaRepository)
            .should(never())
            .save(any(SubcategoriaEntity.class));
        then(subcategoriaRepository).shouldHaveNoMoreInteractions();
        then(categoriaRepository).shouldHaveNoInteractions();
        then(mapper).shouldHaveNoInteractions();
    }

    @Test
    void atualizarSubcategoriaComIdICategoriaInexistenteThrowsException() {
        given(subcategoriaRepository.findById(anyLong())).willReturn(Optional.of(getSubcategoriaEntity()));
        given(subcategoriaRepository.existsByNome(anyString())).willReturn(false);
        given(categoriaRepository.findById(anyLong())).willReturn(Optional.empty());

        final ThrowingCallable sut = () -> manutencaoService.atualizar(1L, getSubcategoriaRegistroDto());

        assertThatThrownBy(sut)
            .isInstanceOf(CategoriaNotFoundException.class)
            .hasMessage("Ops! Nenhuma categoria foi encontrada com o id 1");
        then(subcategoriaRepository)
            .should()
            .findById(anyLong());
        then(subcategoriaRepository)
            .should()
            .existsByNome(anyString());
        then(subcategoriaRepository)
            .should(never())
            .save(any(SubcategoriaEntity.class));
        then(subcategoriaRepository).shouldHaveNoMoreInteractions();
        then(mapper).shouldHaveNoInteractions();
    }

    @Test
    void removerSubcategoriaComIdExistenteRetornaString() {
        given(subcategoriaRepository.findById(anyLong())).willReturn(Optional.of(getSubcategoriaEntity()));

        final var sut = manutencaoService.remover(1L);

        assertThat(sut).isEqualTo("Subcategoria com id 1 removida com sucesso");
        then(subcategoriaRepository)
            .should()
            .deleteById(anyLong());
    }

    @Test
    void removerSubcategoriaComIdInexistenteThrowsException() {
        given(subcategoriaRepository.findById(anyLong())).willReturn(Optional.empty());

        final ThrowingCallable sut = () -> manutencaoService.remover(10L);

        assertThatThrownBy(sut)
            .isInstanceOf(SubcategoriaNotFoundException.class)
            .hasMessage("Ops! Nenhuma subcategoria foi encontrada com o id 10");
        then(subcategoriaRepository)
            .should(never())
            .deleteById(anyLong());
    }

}

