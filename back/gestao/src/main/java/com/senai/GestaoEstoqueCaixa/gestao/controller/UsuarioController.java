/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.controller;

import com.senai.GestaoEstoqueCaixa.gestao.dto.LoginRequestDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.UsuarioRequestDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.UsuarioResponseDTO;
import com.senai.GestaoEstoqueCaixa.gestao.enums.UsuarioEnum;
import com.senai.GestaoEstoqueCaixa.gestao.service.UsuarioService;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Aluno
 */
@RestController
@RequestMapping("/api/v1/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos(
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) UsuarioEnum perfil
    ) {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodos(filtro, ativo, perfil);
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@RequestBody @Valid UsuarioRequestDTO dto) {
        UsuarioResponseDTO criado = usuarioService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@Valid
            @PathVariable Long id,
            @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO atualizado = usuarioService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable @Valid Long id, String emailUsuarioLogado) {
        usuarioService.inativar(id, emailUsuarioLogado);
        return ResponseEntity.noContent().build();
    }

    
    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        UsuarioResponseDTO response = usuarioService.login(dto);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
        public ResponseEntity<UsuarioResponseDTO> buscarId(@PathVariable @Valid Long id) {
        UsuarioResponseDTO response = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }
}
