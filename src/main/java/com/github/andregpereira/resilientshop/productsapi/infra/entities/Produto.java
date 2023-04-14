package com.github.andregpereira.resilientshop.productsapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_produtos", uniqueConstraints = {@UniqueConstraint(name = "uc_sku", columnNames = "sku")})
@SequenceGenerator(name = "produto", sequenceName = "sq_produtos", allocationSize = 1)
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "produto")
    @Column(name = "id_produto")
    private Long id;

    @Column(nullable = false)
    private Long sku;

    @Column(nullable = false, length = 45)
    private String nome;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private BigDecimal valorUnitario;

    @Column(nullable = false)
    private int estoque;

    @ManyToOne
    @JoinColumn(name = "id_subcategoria", nullable = false, foreignKey = @ForeignKey(name = "fk_id_subcategoria"))
    private Subcategoria subcategoria;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Produto produto = (Produto) o;
        return estoque == produto.estoque && Objects.equals(id, produto.id) && Objects.equals(sku,
                produto.sku) && Objects.equals(nome, produto.nome) && Objects.equals(descricao,
                produto.descricao) && Objects.equals(dataCriacao, produto.dataCriacao) && Objects.equals(valorUnitario,
                produto.valorUnitario) && Objects.equals(subcategoria, produto.subcategoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sku, nome, descricao, dataCriacao, valorUnitario, estoque, subcategoria);
    }

}
