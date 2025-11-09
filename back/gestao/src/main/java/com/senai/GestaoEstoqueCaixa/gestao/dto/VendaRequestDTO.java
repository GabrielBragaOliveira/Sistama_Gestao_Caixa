/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Aluno
 */
public record VendaRequestDTO(
        BigDecimal valorRecebido,
        Long usuarioId,
        List<ItemVendaRequestDTO> itens
) {}
