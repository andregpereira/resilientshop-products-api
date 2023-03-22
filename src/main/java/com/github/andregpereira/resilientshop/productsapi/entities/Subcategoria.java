package com.github.andregpereira.resilientshop.productsapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	@JoinColumn(name = "id_categoria", nullable = false, foreignKey = @ForeignKey(name = "fk_id_subcategoria"))
	private Categoria categoria;

}
