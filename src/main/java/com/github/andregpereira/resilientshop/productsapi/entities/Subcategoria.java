package com.github.andregpereira.resilientshop.productsapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    @Column(nullable = false, length = 255)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false, foreignKey = @ForeignKey(name = "fk_id_categoria"))
    private Categoria categoria;

//    @OneToMany(mappedBy = "subcategoria")
//    private List<Produto> produtos;

}
