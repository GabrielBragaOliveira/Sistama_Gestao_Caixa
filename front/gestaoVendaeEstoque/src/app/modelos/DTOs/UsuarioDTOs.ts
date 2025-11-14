import { Perfils } from "../../enum/Perfil";

export interface UsuarioLogin {
  email: string;
  senha: string;
}

export interface UsuarioResponse {
  id: number;
  nome: string;
  email: string;
  perfil: Perfils;
  senha: string;
  ativo: boolean;
}

export interface UsuarioRequest {
  nome: string;
  email: string;
  perfil: Perfils;
  senha: string;
}