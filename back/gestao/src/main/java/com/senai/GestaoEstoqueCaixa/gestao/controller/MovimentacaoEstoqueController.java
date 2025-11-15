/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.controller;

import com.senai.GestaoEstoqueCaixa.gestao.dto.MovimentoEstoqueRequestDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.MovimentoEstoqueResponseDTO;
import com.senai.GestaoEstoqueCaixa.gestao.enums.MovimentoEnum;
import com.senai.GestaoEstoqueCaixa.gestao.service.MovimentacaoEstoqueService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Aluno
 */
@RestController
@RequestMapping("/api/v1/movimentacoes")
@CrossOrigin(origins = "http://localhost:4200")
public class MovimentacaoEstoqueController {

    @Autowired
    private MovimentacaoEstoqueService service;

    public MovimentacaoEstoqueController(MovimentacaoEstoqueService service) {
        this.service = service;
    }

    @PostMapping
public ResponseEntity<MovimentoEstoqueResponseDTO> criarMovimentacao(
        @Valid @RequestBody MovimentoEstoqueRequestDTO dto) {

    MovimentoEstoqueResponseDTO response = service.criarMovimentacao(dto);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
}

    @GetMapping
    public ResponseEntity<List<MovimentoEstoqueResponseDTO>> listarMovimentacoes() {
        return ResponseEntity.ok(service.listarMovimentacoes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimentoEstoqueResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<MovimentoEstoqueResponseDTO>> listarPorProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(service.listarPorProduto(produtoId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<MovimentoEstoqueResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.listarPorUsuario(usuarioId));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<MovimentoEstoqueResponseDTO>> listarPorTipo(@PathVariable String tipo) {
        MovimentoEnum movimentoEnum;
        try {
            movimentoEnum = MovimentoEnum.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(service.listarPorTipo(movimentoEnum));
    }
}
