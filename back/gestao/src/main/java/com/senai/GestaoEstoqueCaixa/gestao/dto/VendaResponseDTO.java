/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Aluno
 */
public record VendaResponseDTO(
        Long id,
        BigDecimal valorTotal,
        BigDecimal valorRecebido,
        BigDecimal troco,
        LocalDate dataVenda,
        Long usuarioId,
        String nomeUsuario,
        List<ItemVendaResponseDTO> itens
) {}