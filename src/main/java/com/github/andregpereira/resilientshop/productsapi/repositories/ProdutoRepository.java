package com.github.andregpereira.resilientshop.productsapi.repositories;

import com.github.andregpereira.resilientshop.productsapi.infra.entities.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    boolean existsBySku(Long sku);

    boolean existsByNome(String nome);

    @Query(value = "select * from tb_produtos p where p.nome ilike %:nome%", nativeQuery = true)
    Page<Produto> findByNome(@Param("nome") String nome, Pageable pageable);

    Page<Produto> findAllBySubcategoriaId(Long id, Pageable pageable);

    Page<Produto> findAllBySubcategoriaCategoriaId(Long id, Pageable pageable);

}
