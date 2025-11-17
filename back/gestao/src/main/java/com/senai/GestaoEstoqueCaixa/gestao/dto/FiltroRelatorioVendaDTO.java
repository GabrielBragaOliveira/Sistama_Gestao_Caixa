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
 * @author CacatsViado
 */
public record FiltroRelatorioVendaDTO(
        LocalDate dataInicial,
        LocalDate dataFinal,
        BigDecimal valorMin,
        BigDecimal valorMax,
        Long usuarioId
) {}