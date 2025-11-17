/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.dto;

import java.time.LocalDate;

/**
 *
 * @author Aluno
 */
public record RelatorioMovimentoEstoqueResponseDTO(
        Long id,
        String tipo,
        Integer quantidade,
        LocalDate data
) {}
