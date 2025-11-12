/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.mapper;

import com.senai.GestaoEstoqueCaixa.gestao.dto.MovimentoEstoqueRequestDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.MovimentoEstoqueResponseDTO;
import com.senai.GestaoEstoqueCaixa.gestao.entity.MovimentacaoEstoque;
import com.senai.GestaoEstoqueCaixa.gestao.entity.Produto;
import com.senai.GestaoEstoqueCaixa.gestao.entity.Usuario;
import com.senai.GestaoEstoqueCaixa.gestao.enums.MovimentoEnum;
import java.time.LocalDateTime;

/**
 *
 * @author Aluno
 */
public class MovimentoMapper {

    public static MovimentoEstoqueResponseDTO toResponseDTO(MovimentacaoEstoque movimentacao) {
        return new MovimentoEstoqueResponseDTO(
                movimentacao.getId(),
                movimentacao.getProduto().getId(),
                movimentacao.getTipo().name(),
                movimentacao.getQuantidade(),
                movimentacao.getMotivo(),
                movimentacao.getData(),
                movimentacao.getUsuarioResponsavel() != null ? movimentacao.getUsuarioResponsavel().getId() : null
        );
    }

    public static MovimentacaoEstoque toEntity(MovimentoEstoqueRequestDTO dto, Produto produto, Usuario usuario) {
        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setProduto(produto);
        movimentacao.setTipo(MovimentoEnum.valueOf(dto.tipo()));
        movimentacao.setQuantidade(dto.quantidade());
        movimentacao.setMotivo(dto.motivo());
        movimentacao.setUsuarioResponsavel(usuario);
        movimentacao.setData(LocalDateTime.now());
        return movimentacao;
    }
}