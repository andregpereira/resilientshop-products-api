package com.github.andregpereira.resilientshop.productsapi.infra.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
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

    @Column(nullable = false)
    private String descricao;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private BigDecimal valorUnitario;

    @Column(nullable = false)
    private int estoque;

    @Column(nullable = false)
    private boolean ativo;

    @ManyToOne
    @JoinColumn(name = "id_subcategoria", nullable = false, foreignKey = @ForeignKey(name = "fk_id_subcategoria"))
    private Subcategoria subcategoria;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Produto produto))
            return false;
        return estoque == produto.estoque && ativo == produto.ativo && Objects.equals(id, produto.id) && Objects.equals(
                sku, produto.sku) && Objects.equals(nome, produto.nome) && Objects.equals(descricao,
                produto.descricao) && Objects.equals(dataCriacao, produto.dataCriacao) && Objects.equals(valorUnitario,
                produto.valorUnitario) && Objects.equals(subcategoria, produto.subcategoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sku, nome, descricao, dataCriacao, valorUnitario, estoque, ativo, subcategoria);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Produto.class.getSimpleName() + "[", "]").add("id=" + id).add("sku=" + sku).add(
                "nome='" + nome + "'").add("descricao='" + descricao + "'").add("dataCriacao=" + dataCriacao).add(
                "valorUnitario=" + valorUnitario).add("estoque=" + estoque).add("ativo=" + ativo).add(
                "subcategoria=" + subcategoria).toString();
    }

}
