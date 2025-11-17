/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.dto;

import java.math.BigDecimal;

/**
 *
 * @author Aluno
 */
public record VendasPorMesResponseDTO(
        Integer ano,
        Integer mes,
        Long quantidadeVendas,
        BigDecimal totalVendido
) {}