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
@Table(name = "tb_categorias")
@SequenceGenerator(name = "categoria", sequenceName = "sq_categorias", allocationSize = 1)
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categoria")
    @Column(name = "id_categoria")
    private Long id;

    @Column(length = 45, nullable = false)
    private String nome;

//    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
//    private List<Subcategoria> subcategorias;

}
