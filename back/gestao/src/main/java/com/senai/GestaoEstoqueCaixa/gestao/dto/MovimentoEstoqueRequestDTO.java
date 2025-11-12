/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author Aluno
 */
public record MovimentoEstoqueRequestDTO(
        @NotNull(message = "O ID do produto é obrigatório.")
        Long produtoId,

        @NotBlank(message = "O tipo de movimentação é obrigatório.")
        String tipo, // ENTRADA, AJUSTE, SAIDA

        @NotNull(message = "A quantidade é obrigatória.")
        @Min(value = 1, message = "A quantidade deve ser no mínimo 1.")
        Integer quantidade,

        String motivo

) {}
