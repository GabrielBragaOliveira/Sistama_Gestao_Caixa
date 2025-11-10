/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.mapper;

import com.senai.GestaoEstoqueCaixa.gestao.dto.ItemVendaRequestDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.ItemVendaResponseDTO;
import com.senai.GestaoEstoqueCaixa.gestao.entity.ItemVenda;
import com.senai.GestaoEstoqueCaixa.gestao.entity.Produto;
import java.math.BigDecimal;

/**
 *
 * @author Aluno
 */
public class ItemVendaMapper {

    public static ItemVenda toEntity(ItemVendaRequestDTO dto, Produto produto) {
        ItemVenda item = new ItemVenda();
        item.setProduto(produto);
        item.setQuantidade(dto.quantidade());
        item.setPrecoUnitario(dto.precoUnitario());
        item.setSubtotal(dto.precoUnitario().multiply(BigDecimal.valueOf(dto.quantidade())));
        return item;
    }

    public static ItemVendaResponseDTO toResponseDTO(ItemVenda item) {
        return new ItemVendaResponseDTO(
                item.getId(),
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getSubtotal()
        );
    }
}