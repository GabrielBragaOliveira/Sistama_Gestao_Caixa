/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.controller;

import com.senai.GestaoEstoqueCaixa.gestao.dto.RelatorioVendasDTO;
import com.senai.GestaoEstoqueCaixa.gestao.service.RelatorioService;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/vendas")
    public ResponseEntity<RelatorioVendasDTO> getRelatorio(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            @RequestParam(required = false) BigDecimal valorMin,
            @RequestParam(required = false) BigDecimal valorMax,
            @RequestParam(required = false) Long usuarioId) {

        RelatorioVendasDTO relatorio = relatorioService.gerarRelatorio(dataInicial, dataFinal, valorMin, valorMax, usuarioId);
        return ResponseEntity.ok(relatorio);
    }
}
