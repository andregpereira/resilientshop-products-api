package com.github.andregpereira.resilientshop.productsapi.infra.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Entity(name = "Categoria")
@Table(name = "tb_categorias")
@SequenceGenerator(name = "categoria", sequenceName = "sq_categorias", allocationSize = 1)
public class CategoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categoria")
    @Column(name = "id_categoria")
    private Long id;

    @Column(length = 45, nullable = false)
    private String nome;

    @ToString.Exclude
    @OneToMany(mappedBy = "categoria")
    private Set<SubcategoriaEntity> subcategorias = new LinkedHashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "categoria")
    private Set<ProdutoEntity> produtos = new LinkedHashSet<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoriaEntity categoria)) {
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
        return getId() != null && Objects.equals(getId(), categoria.getId());
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
