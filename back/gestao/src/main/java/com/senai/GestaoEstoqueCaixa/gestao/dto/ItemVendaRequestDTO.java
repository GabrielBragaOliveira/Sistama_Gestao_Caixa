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
public record ItemVendaRequestDTO(
        Long produtoId,
        Integer quantidade,
        BigDecimal precoUnitario
) {}