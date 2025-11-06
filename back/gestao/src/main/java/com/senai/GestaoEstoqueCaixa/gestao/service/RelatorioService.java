/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.service;

import com.senai.GestaoEstoqueCaixa.gestao.dto.RelatorioVendasDTO;
import com.senai.GestaoEstoqueCaixa.gestao.entity.ItemVenda;
import com.senai.GestaoEstoqueCaixa.gestao.entity.Venda;
import com.senai.GestaoEstoqueCaixa.gestao.repository.VendaRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Aluno
 */
@Service
public class RelatorioService {

    @Autowired
    private VendaRepository vendaRepository;

    public RelatorioVendasDTO gerarRelatorio(LocalDate dataInicial,
                                             LocalDate dataFinal,
                                             BigDecimal valorMin,
                                             BigDecimal valorMax,
                                             Long usuarioId) {

        List<Venda> vendas = vendaRepository.filtrarVendas(dataInicial, dataFinal, valorMin, valorMax, usuarioId);

        BigDecimal totalVendas = vendas.stream()
                .map(Venda::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalItens = vendas.stream()
                .flatMap(v -> v.getItens().stream())
                .mapToLong(ItemVenda::getQuantidade)
                .sum();
        return new RelatorioVendasDTO(vendas, totalVendas, totalItens);
    }
}
