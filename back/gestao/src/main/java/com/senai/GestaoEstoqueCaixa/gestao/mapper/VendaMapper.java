/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.mapper;

import com.senai.GestaoEstoqueCaixa.gestao.dto.ItemVendaResponseDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.VendaRequestDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.VendaResponseDTO;
import com.senai.GestaoEstoqueCaixa.gestao.entity.ItemVenda;
import com.senai.GestaoEstoqueCaixa.gestao.entity.Usuario;
import com.senai.GestaoEstoqueCaixa.gestao.entity.Venda;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author CacatsViado
 */
public class VendaMapper {

    public static VendaResponseDTO toResponseDTO(Venda venda) {
        List<ItemVendaResponseDTO> itensDTO = venda.getItens()
                .stream()
                .map(ItemVendaMapper::toResponseDTO)
                .collect(Collectors.toList());

        return new VendaResponseDTO(
                venda.getId(),
                venda.getValorTotal(),
                venda.getValorRecebido(),
                venda.getTroco(),
                venda.getDataVenda(),
                venda.getUsuarioResponsavel() != null ? venda.getUsuarioResponsavel().getId() : null,
                venda.getUsuarioResponsavel() != null ? venda.getUsuarioResponsavel().getNome() : null,
                itensDTO
        );
    }

    public static Venda toEntity(VendaRequestDTO dto, Usuario usuario, List<ItemVenda> itens) {
        Venda venda = new Venda();
        venda.setUsuarioResponsavel(usuario);
        venda.setItens(itens);

        BigDecimal total = itens.stream()
                .map(ItemVenda::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        venda.setValorTotal(total);
        venda.setValorRecebido(dto.valorRecebido());

        BigDecimal troco = dto.valorRecebido().subtract(total);
        venda.setTroco(troco.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : troco);

        return venda;
    }
}