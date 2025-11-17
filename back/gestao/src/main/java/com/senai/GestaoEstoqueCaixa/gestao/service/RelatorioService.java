/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.service;

import com.senai.GestaoEstoqueCaixa.gestao.dto.FiltroRelatorioVendaDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.RelatorioDetalheVendaResponse;
import com.senai.GestaoEstoqueCaixa.gestao.dto.RelatorioMovimentoEstoqueResponseDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.RelatorioResumoVendaResponse;
import com.senai.GestaoEstoqueCaixa.gestao.dto.UsuarioResumoDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.VendasPorMesResponseDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.VendasPorOperadorResponseDTO;
import com.senai.GestaoEstoqueCaixa.gestao.entity.Usuario;
import com.senai.GestaoEstoqueCaixa.gestao.entity.Venda;
import com.senai.GestaoEstoqueCaixa.gestao.enums.MovimentoEnum;
import com.senai.GestaoEstoqueCaixa.gestao.exceptions.RecursoNaoEncontradoException;
import com.senai.GestaoEstoqueCaixa.gestao.mapper.MovimentoMapper;
import com.senai.GestaoEstoqueCaixa.gestao.mapper.VendaMapper;
import com.senai.GestaoEstoqueCaixa.gestao.repository.MovimentacaoEstoqueRepository;
import com.senai.GestaoEstoqueCaixa.gestao.repository.UsuarioRepository;
import com.senai.GestaoEstoqueCaixa.gestao.repository.VendaRepository;
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

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public List<RelatorioResumoVendaResponse> gerarResumo(FiltroRelatorioVendaDTO filtro) {

        return vendaRepository.gerarRelatorioResumoComFiltros(
                filtro.dataInicial(),
                filtro.dataFinal(),
                filtro.valorMin(),
                filtro.valorMax(),
                filtro.usuarioId()
        );
    }

    public RelatorioDetalheVendaResponse detalhes(Long vendaId) {
        Venda venda = vendaRepository.findById(vendaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Venda não encontrada"));

        Usuario usuario = venda.getUsuarioResponsavel();

        UsuarioResumoDTO userDTO = new UsuarioResumoDTO(
                usuario.getNome(),
                usuario.getEmail()
        );

        return new RelatorioDetalheVendaResponse(
                VendaMapper.toResponseDTO(venda),
                userDTO
        );
    }

    public List<VendasPorOperadorResponseDTO> vendasPorOperador() {
        return vendaRepository.relatorioVendasPorOperador();
    }

    public List<VendasPorMesResponseDTO> vendasPorMes() {
        return vendaRepository.relatorioVendasPorMes();
    }

    public List<RelatorioMovimentoEstoqueResponseDTO> listarMovimentacoesPorProduto(Long produtoId) {

        return movimentacaoEstoqueRepository.findByProdutoId(produtoId)
                .stream()
                .filter(mov -> mov.getTipo() == MovimentoEnum.ENTRADA
                || mov.getTipo() == MovimentoEnum.SAIDA)
                .map(MovimentoMapper::toRelatorioDTO)
                .toList();
    }
}
