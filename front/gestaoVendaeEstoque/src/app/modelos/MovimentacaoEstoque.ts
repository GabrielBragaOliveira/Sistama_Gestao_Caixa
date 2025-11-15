import { Produto } from "./Produto";
import { Usuario } from "./Usuario";

export interface MovimentacaoEstoque {
    id?: number,
    produtoId: Produto,
    tipo: string,
    quantidade: number,
    motivo: string,
    data: string,
    usuarioId: Usuario,
}
