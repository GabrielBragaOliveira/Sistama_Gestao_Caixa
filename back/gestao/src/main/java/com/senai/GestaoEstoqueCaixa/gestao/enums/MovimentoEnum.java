/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.enums;

/**
 *
 * @author Aluno
 */
public enum MovimentoEnum {
    ENTRADA, //reposição de estoque
    AJUSTE, //Correção de erro: Erro de contagem, produto danificado, perdal
    SAIDA  //remoção de estoque: quebra, venda. produto usado internamente
}
