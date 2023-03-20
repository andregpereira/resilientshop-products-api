package com.github.andregpereira.resilientshop.productsapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.andregpereira.resilientshop.productsapi.entities.Subcategoria;

@Repository
public interface SubcategoriaRepository extends JpaRepository<Subcategoria, Long> {

}
