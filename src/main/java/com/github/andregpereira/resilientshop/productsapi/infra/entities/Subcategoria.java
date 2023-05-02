package com.github.andregpereira.resilientshop.productsapi.infra.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

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
        if (o == null || getClass() != o.getClass())
            return false;
        Subcategoria that = (Subcategoria) o;
        return Objects.equals(id, that.id) && Objects.equals(nome, that.nome) && Objects.equals(descricao,
                that.descricao) && Objects.equals(categoria, that.categoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, descricao, categoria);
    }

}
