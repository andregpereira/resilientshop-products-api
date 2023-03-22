package com.github.andregpereira.resilientshop.productsapi.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.andregpereira.resilientshop.productsapi.entities.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

	boolean existsByNome(String nome);

	Page<Categoria> findByNome(String nome, Pageable pageable);

}
