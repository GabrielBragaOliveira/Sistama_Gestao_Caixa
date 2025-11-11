export interface ProdutoRequest{
  codigo: string;
  nome: string;
  categoria: string;
  quantidadeEstoque: number;
  preco: number;
}

export interface ProdutoResponse{
  id: number;
  codigo: string;
  nome: string;
  categoria: string;
  quantidadeEstoque: number;
  preco: number;
  ativo: boolean;
}

