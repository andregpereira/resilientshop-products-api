package com.github.andregpereira.resilientshop.productsapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
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

	@Column(nullable = false, length = 45)
	private String nome;

	@OneToMany
	@PrimaryKeyJoinColumn(nome = "id_subcategoria")
	private Subcategoria subcategoria;

}
