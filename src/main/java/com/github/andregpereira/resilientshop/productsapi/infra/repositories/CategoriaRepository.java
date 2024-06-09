package com.github.andregpereira.resilientshop.productsapi.infra.repositories;

import com.github.andregpereira.resilientshop.productsapi.infra.entities.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Long> {

    boolean existsByNome(String nome);

}
