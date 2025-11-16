/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Aluno
 */
public record MovimentoEstoqueResponseDTO(
        Long id,
        Long produtoId,
        String tipo,
        Integer quantidade,
        String motivo,
        LocalDate data,
        Long usuarioId
) {}