package com.github.andregpereira.resilientshop.productsapi.infra.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Produto")
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tb_produtos", uniqueConstraints = {@UniqueConstraint(name = "uc_sku", columnNames = "sku")})
@SequenceGenerator(name = "produto", sequenceName = "sq_produtos", allocationSize = 1)
public class ProdutoEntity {

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

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal valorUnitario;

    @Column(nullable = false)
    private int estoque;

    @Column(precision = 2, scale = 1)
    private BigDecimal rating = BigDecimal.valueOf(3.6);

    private String imageUrl;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @LastModifiedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataModificacao;

    @Column(nullable = false)
    private boolean ativo;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false, foreignKey = @ForeignKey(name = "fk_id_categoria"))
    private CategoriaEntity categoria;

    @ManyToOne
    @JoinColumn(name = "id_subcategoria", foreignKey = @ForeignKey(name = "fk_id_subcategoria"))
    private SubcategoriaEntity subcategoria;

    @Override
    public final boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ProdutoEntity produto))
            return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass)
            return false;
        return getId() != null && Objects.equals(getId(), produto.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

}
