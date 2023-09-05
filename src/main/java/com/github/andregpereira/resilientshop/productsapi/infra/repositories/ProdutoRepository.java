package com.github.andregpereira.resilientshop.productsapi.infra.repositories;

import com.github.andregpereira.resilientshop.productsapi.infra.entities.ProdutoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {

    boolean existsBySku(Long sku);

    boolean existsByNome(String nome);

    Optional<ProdutoEntity> findByIdAndAtivoTrue(Long id);

    Optional<ProdutoEntity> findByIdAndAtivoFalse(Long id);

    @Query(value = """
            SELECT * FROM tb_produtos p
            WHERE p.nome ILIKE %:nome%
            """, nativeQuery = true)
    Page<ProdutoEntity> findByNome(@Param("nome") String nome, Pageable pageable);

    Page<ProdutoEntity> findAllBySubcategoriaId(Long id, Pageable pageable);

    Page<ProdutoEntity> findAllBySubcategoriaCategoriaId(Long id, Pageable pageable);

}
