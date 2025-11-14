/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 *
 * @author Aluno
 */

public record ProdutoRequestDTO(

    @NotBlank(message = "O nome é obrigatório.")
    String nome,
        
    @NotNull(message = "O código é obrigatório.")
    @Min(value = 1000000, message = "O código deve conter exatamente 7 dígitos.")
    @Max(value = 9999999, message = "O código deve conter exatamente 7 dígitos.")
    Integer codigo,

    @NotBlank(message = "A categoria é obrigatória.")
    String categoria,

    @NotNull(message = "O preço é obrigatório.")
    @Min(value = 0, message = "O preço não pode ser negativo.")
    BigDecimal preco

) {}
