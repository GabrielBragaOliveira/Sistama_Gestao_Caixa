export interface ProdutoRequest {
  codigo: number;
  nome: string;
  categoria: string;
  preco: number;
}

export interface ProdutoResponse {
  id: number;
  codigo: number;
  nome: string;
  categoria: string;
  quantidadeEstoque: number;
  preco: number;
  ativo: boolean;
}