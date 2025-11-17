/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.repository;

import com.senai.GestaoEstoqueCaixa.gestao.entity.Produto;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Aluno
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findById(Long id);

    Optional<Produto> findByCodigo(Integer codigo);

    boolean existsByCodigo(Integer codigo);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Produto p WHERE p.id = :id")
    Optional<Produto> findByIdForUpdate(@Param("id") Long id);

}
