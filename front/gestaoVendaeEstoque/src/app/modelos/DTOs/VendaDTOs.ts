import { ProdutoResponse } from "./ProdutoDTO";
import { UsuarioResponse } from "./UsuarioDTOs";

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
    nome: string;
    email: string;
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