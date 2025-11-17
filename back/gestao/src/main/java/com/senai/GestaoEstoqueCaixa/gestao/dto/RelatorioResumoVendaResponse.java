/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Aluno
 */
public record RelatorioResumoVendaResponse(
        Long vendaId,
        BigDecimal valorTotal,
        LocalDate dataVenda,
        String nome,
        String email
) {}