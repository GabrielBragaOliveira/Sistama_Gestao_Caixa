/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.dto;

/**
 *
 * @author CacatsViado
 */
public record RelatorioDetalheVendaResponse(
        VendaResponseDTO venda,
        UsuarioResumoDTO usuario
) {}
