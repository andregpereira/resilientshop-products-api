package com.github.andregpereira.resilientshop.productsapi.infra.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Subcategoria")
@Table(name = "tb_subcategorias")
@SequenceGenerator(name = "subcategoria", sequenceName = "sq_subcategorias", allocationSize = 1)
public class SubcategoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subcategoria")
    @Column(name = "id_subcategoria")
    private Long id;

    @Column(nullable = false, length = 45)
    private String nome;

    @Column(nullable = false)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false, foreignKey = @ForeignKey(name = "fk_categoria"))
    private CategoriaEntity categoria;

    @ToString.Exclude
    @OneToMany(mappedBy = "subcategoria")
    private Set<ProdutoEntity> produtos = new LinkedHashSet<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubcategoriaEntity subcategoria)) {
            return false;
        }
        final var oEffectiveClass = o instanceof HibernateProxy hibernateProxy
            ? hibernateProxy
            .getHibernateLazyInitializer()
            .getPersistentClass()
            : o.getClass();
        final var thisEffectiveClass = this instanceof HibernateProxy hibernateProxy
            ? hibernateProxy
            .getHibernateLazyInitializer()
            .getPersistentClass()
            : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) {
            return false;
        }
        return getId() != null && Objects.equals(getId(), subcategoria.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hibernateProxy
            ? hibernateProxy
            .getHibernateLazyInitializer()
            .getPersistentClass()
            .hashCode()
            : getClass().hashCode();
    }

}
