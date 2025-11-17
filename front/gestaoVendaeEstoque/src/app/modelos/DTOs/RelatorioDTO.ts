
export interface RelatorioResponse {
  vendaId: number;    
  dataVenda: string;
  valorTotal: number; 
  nome: string;
  email: string;
}

export interface RelatorioDetalhe {
  venda: Venda;      
  usuario: UsuarioDetalhe;   
}

export interface Venda {
  id: number;
  valorTotal: number;
  valorRecebido: number;
  troco: number;
  dataVenda: string;
  usuarioId: number;
  nomeUsuario: string;
  itens: ItemVendaDetalhe[];
}

export interface UsuarioDetalhe {
  nome: string;
  email: string;
}

export interface ItemVendaDetalhe {
  id: number;
  produtoId: number;
  nomeProduto: string;
  quantidade: number;
  precoUnitario: number;
  subtotal: number;
}