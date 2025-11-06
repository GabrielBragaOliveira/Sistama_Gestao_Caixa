/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.dto;

import com.senai.GestaoEstoqueCaixa.gestao.entity.Venda;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Aluno
 */
public record RelatorioVendasDTO(
        List<Venda> vendas,
        BigDecimal totalVendas,
        Long totalItensVendidos) {

}
