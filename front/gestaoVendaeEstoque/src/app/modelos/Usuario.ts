import { Perfils } from "../enum/Perfil";

export interface Usuario {
  id: number;
  nome: string;
  email: string;
  perfil: Perfils;
  senha: string;
  ativo: boolean;
}