import { Usuario } from "../Usuario";
import { ProdutoResponse } from "./ProdutoDTO";

export interface VendaRequest {
    valorRecebido: number;
    usuarioId: number;
    itens: itemvendaRequest[];
}

export interface VendaReponse {
    id: number;
    valortotal: number;
    valorRecebido: number;
    troco: number;
    dataVenda: string;
    usuarioResponsavel: Usuario;
    listaItens: itemVenda[];
}

export interface itemVenda {
    produto: ProdutoResponse;
    quantidade: number;
}

export interface itemvendaRequest{
    produtoId: number;
    quantidade: number;
    precoUnitario: number;
}