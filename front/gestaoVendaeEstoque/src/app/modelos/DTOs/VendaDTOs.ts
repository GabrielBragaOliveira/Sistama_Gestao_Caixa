import { Usuario } from "../Usuario";
import { ProdutoResponse } from "./ProdutoDTO";

export interface VendaRequest {
    valortotal: number;
    valorRecebido: number;
    troco: number;
    dataVenda: string;
    usuarioResponsavel: Usuario;
    listaItens: itemvendaResponse[];
}

export interface VendaReponse {
    id: number;
    valortotal: number;
    valorRecebido: number;
    troco: number;
    dataVenda: string;
    usuarioResponsavel: Usuario;
    listaItens: itemVendaRequest[];
}

export interface itemVendaRequest {
    produto: ProdutoResponse;
    quantidade: number;
}

export interface itemvendaResponse {
    produtoId: number;
    quantidade: number;
}