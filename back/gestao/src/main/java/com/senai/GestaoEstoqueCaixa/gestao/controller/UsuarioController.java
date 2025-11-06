/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.controller;

import com.senai.GestaoEstoqueCaixa.gestao.dto.UsuarioRequestDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.UsuarioResponseDTO;
import com.senai.GestaoEstoqueCaixa.gestao.service.UsuarioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Aluno
 */
@RestController
@RequestMapping("/api/v1/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }
    
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO criado = usuarioService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{email}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @PathVariable String email,
            @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO atualizado = usuarioService.atualizar(email, dto);
        return ResponseEntity.ok(atualizado);
    }
    
    @PatchMapping("/{email}/deletar")
    public ResponseEntity<Void> deletar(@PathVariable String email) {
        usuarioService.deletar(email);
        return ResponseEntity.noContent().build();
    }
}
