package com.github.andregpereira.resilientshop.productsapi.infra.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.StringJoiner;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_subcategorias")
@SequenceGenerator(name = "subcategoria", sequenceName = "sq_subcategorias", allocationSize = 1)
public class Subcategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subcategoria")
    @Column(name = "id_subcategoria")
    private Long id;

    @Column(nullable = false, length = 45)
    private String nome;

    @Column(nullable = false)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false, foreignKey = @ForeignKey(name = "fk_id_categoria"))
    private Categoria categoria;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Subcategoria subcategoria))
            return false;
        return Objects.equals(id, subcategoria.id) && Objects.equals(nome, subcategoria.nome) && Objects.equals(
                descricao, subcategoria.descricao) && Objects.equals(categoria, subcategoria.categoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, descricao, categoria);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Subcategoria.class.getSimpleName() + "[", "]").add("id=" + id).add(
                "nome='" + nome + "'").add("descricao='" + descricao + "'").add("categoria=" + categoria).toString();
    }

}
