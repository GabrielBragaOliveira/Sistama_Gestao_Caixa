/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.repository;

import com.senai.GestaoEstoqueCaixa.gestao.entity.Venda;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Aluno
 */
@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    @Query("SELECT v FROM Venda v WHERE " +
           "(:dataInicial IS NULL OR v.dataVenda >= :dataInicial) AND " +
           "(:dataFinal IS NULL OR v.dataVenda <= :dataFinal) AND " +
           "(:valorMin IS NULL OR v.valorTotal >= :valorMin) AND " +
           "(:valorMax IS NULL OR v.valorTotal <= :valorMax) AND " +
           "(:usuarioId IS NULL OR v.usuarioResponsavel.id = :usuarioId)")
    List<Venda> filtrarVendas(
        @Param("dataInicial") LocalDate dataInicial,
        @Param("dataFinal") LocalDate dataFinal,
        @Param("valorMin") BigDecimal valorMin,
        @Param("valorMax") BigDecimal valorMax,
        @Param("usuarioId") Long usuarioId
    );
}