package com.github.andregpereira.resilientshop.productsapi.dtos.subcategoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record SubcategoriaRegistroDto(@NotBlank(message = "O nome é obrigatório") @Size(
        message = "O nome não pode ter menos de 3 e mais de 45 caracteres", min = 3, max = 45) String nome,
        @NotBlank(message = "A descrição é obrigatória") @Size(
                message = "A descrição não pode ter menos de 15 e mais de 255 caracteres") String descricao,
        @NotNull(message = "A categoria é obrigatória") @Positive(
                message = "Poxa! O id deve ser maior que 0") Long idCategoria) {

}
