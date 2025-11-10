/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.controller;

import com.senai.GestaoEstoqueCaixa.gestao.dto.ItemVendaRequestDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.ItemVendaResponseDTO;
import com.senai.GestaoEstoqueCaixa.gestao.service.ItemVendaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Aluno
 */
@RestController
@RequestMapping("/api/v1/itens-venda")
@CrossOrigin(origins = "http://localhost:4200")
public class ItemVendaController {

    @Autowired
    private ItemVendaService itemVendaService;

    public ItemVendaController(ItemVendaService itemVendaService) {
        this.itemVendaService = itemVendaService;
    }

    @PostMapping
    public ResponseEntity<ItemVendaResponseDTO> criarItemVenda(@Valid @RequestBody ItemVendaRequestDTO dto) {
        ItemVendaResponseDTO novoItem = itemVendaService.criarItemVenda(dto);
        return ResponseEntity.ok(novoItem);
    }

    @GetMapping
    public ResponseEntity<List<ItemVendaResponseDTO>> listarTodos() {
        List<ItemVendaResponseDTO> itens = itemVendaService.listarTodos();
        return ResponseEntity.ok(itens);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemVendaResponseDTO> buscarPorId(@PathVariable Long id) {
        ItemVendaResponseDTO item = itemVendaService.buscarPorId(id);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarItem(@PathVariable Long id) {
        itemVendaService.deletarItem(id);
        return ResponseEntity.noContent().build();
    }
}