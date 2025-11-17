import { Venda } from "./Venda"

export interface Relatorio {
    vendas: Venda[],
    totalVendas: number,
    totalItensVendidos: number
}
