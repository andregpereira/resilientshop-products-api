package com.github.andregpereira.resilientshop.productsapi.repositories;

import com.github.andregpereira.resilientshop.productsapi.entities.Subcategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubcategoriaRepository extends JpaRepository<Subcategoria, Long> {

    boolean existsByNome(String nome);

}
