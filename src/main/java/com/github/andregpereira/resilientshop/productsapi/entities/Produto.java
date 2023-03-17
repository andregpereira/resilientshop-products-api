package com.github.andregpereira.resilientshop.productsapi.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@SequenceGenerator(name = "produto", sequenceName = "sq_produto", allocationSize = 1)
@Table(name = "tb_produtos", uniqueConstraints = { @UniqueConstraint(name = "uc_sku", columnNames = "sku") })
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "produto")
	@Column(name = "id_produto")
	private Long id;

	@Column(nullable = false)
	private Long sku;

	@Column(nullable = false, length = 45)
	private String nome;

	@Column
	private String descricao;

	@Column(nullable = false, updatable = false)
	private LocalDateTime dataCriacao;

	@Column(nullable = false)
	private BigDecimal valorUnitario;

	@Column(nullable = false)
	private Integer estoque;

}
