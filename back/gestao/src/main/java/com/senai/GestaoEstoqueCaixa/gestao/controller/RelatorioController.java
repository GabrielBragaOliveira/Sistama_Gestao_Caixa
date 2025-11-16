/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.controller;

import com.senai.GestaoEstoqueCaixa.gestao.dto.FiltroRelatorioVendaDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.MovimentoEstoqueResponseDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.RelatorioDetalheVendaResponse;
import com.senai.GestaoEstoqueCaixa.gestao.dto.RelatorioResumoVendaResponse;
import com.senai.GestaoEstoqueCaixa.gestao.dto.VendasPorMesResponseDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.VendasPorOperadorResponseDTO;
import com.senai.GestaoEstoqueCaixa.gestao.service.RelatorioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Aluno
 */
@RestController
@RequestMapping("/api/v1/relatorios")
@CrossOrigin(origins = "http://localhost:4200")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/resumo")
    public List<RelatorioResumoVendaResponse> gerarResumo(FiltroRelatorioVendaDTO filtro) {
        return relatorioService.gerarResumo(filtro);
    }

    @GetMapping("/venda/{id}")
    public ResponseEntity<RelatorioDetalheVendaResponse> detalhes(@PathVariable Long id) {
        return ResponseEntity.ok(relatorioService.detalhes(id));
    }

    @GetMapping("/vendas/por-operador")
    public List<VendasPorOperadorResponseDTO> vendasPorOperador() {
        return relatorioService.vendasPorOperador();
    }

    @GetMapping("/vendas/por-mes")
    public List<VendasPorMesResponseDTO> vendasPorMes() {
        return relatorioService.vendasPorMes();
    }

    @GetMapping("/movimentacoes/produto/{produtoId}")
    public List<MovimentoEstoqueResponseDTO> listarMovimentacoesPorProduto(@PathVariable Long produtoId) {
        return relatorioService.listarMovimentacoesPorProduto(produtoId);
    }

}
