import { Produto } from "./Produto";
import { Usuario } from "./Usuario";

export interface Venda{
    id:Number;
    valortotal:number;
    valorRecebido:number;
    troco:number;
    dataVenda:string;
    usuarioResponsavel:Usuario;
    listaItens:Produto[];
}