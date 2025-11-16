/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.repository;

import com.senai.GestaoEstoqueCaixa.gestao.dto.RelatorioResumoVendaResponse;
import com.senai.GestaoEstoqueCaixa.gestao.entity.Venda;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Query("""
        SELECT new com.senai.GestaoEstoqueCaixa.gestao.dto.RelatorioResumoVendaResponse(
            v.id,
            v.valorTotal,
            v.dataVenda,
            u.nome,
            u.email
        )
        FROM Venda v
        JOIN v.usuarioResponsavel u
        WHERE
            (:dataInicial IS NULL OR v.dataVenda >= :dataInicial) AND
            (:dataFinal IS NULL OR v.dataVenda <= :dataFinal) AND
            (:valorMin IS NULL OR v.valorTotal >= :valorMin) AND
            (:valorMax IS NULL OR v.valorTotal <= :valorMax) AND
            (:usuarioId IS NULL OR u.id = :usuarioId)
    """)
    List<RelatorioResumoVendaResponse> gerarRelatorioResumoComFiltros(
            @Param("dataInicial") LocalDate  dataInicial,
            @Param("dataFinal") LocalDate  dataFinal,
            @Param("valorMin") BigDecimal valorMin,
            @Param("valorMax") BigDecimal valorMax,
            @Param("usuarioId") Long usuarioId
    );
}