package com.github.andregpereira.resilientshop.productsapi.app.dtos.produto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProdutoRegistroDto(@NotNull(message = "O SKU é obrigatório") Long sku,
        @NotBlank(message = "O nome é obrigatório") @Size(
                message = "Ops! O nome do produto deve ter entre 2 e 45 caracteres. Verifique e tente novamente",
                min = 2, max = 45) String nome,
        @NotBlank(message = "A descrição é obrigatória") @Size(
                message = "Poxa vida! A descrição está fora do tamanho esperado. Digite de 15 a 255 caracteres",
                min = 15, max = 255) String descricao,
        @NotNull(message = "O valor unitário é obrigatório") @Positive(
                message = "Poxa! O valor não pode ser igual ou inferior a R$0,00") BigDecimal valorUnitario,
        @NotNull(message = "A quantidade em estoque é obrigatória") @Positive(
                message = "Ops! Quantidade inválida, digite um número que não seja negativo ou zero") int estoque,
        @NotNull(message = "A subcategoria é obrigatória") @Positive(
                message = "O valor mínimo para esse campo é 1") Long idSubcategoria) {

}
