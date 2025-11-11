export interface Produto{
  id: number;
  codigo: number;
  nome: string;
  categoria: string;
  quantidadeEstoque: number;
  preco: number;
  ativo: boolean;
}