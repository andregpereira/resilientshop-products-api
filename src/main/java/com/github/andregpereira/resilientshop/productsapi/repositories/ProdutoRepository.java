package com.github.andregpereira.resilientshop.productsapi.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.andregpereira.resilientshop.productsapi.entities.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	Optional<Produto> findBySku(Long sku);

	@Query(value = "select * from tb_produtos p where p.nome ilike %:nome%", nativeQuery = true)
	Page<Produto> findByNome(@Param("nome") String nome, Pageable pageable);

}
