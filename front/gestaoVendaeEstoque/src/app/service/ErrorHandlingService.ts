import { Injectable } from '@angular/core';
import { MessageService } from 'primeng/api';

@Injectable({
  providedIn: 'root' 
})

export class ErrorHandlingService {

  constructor(private msg: MessageService) { }

  public tratarErroHttp(err: any): void {
    const status = err?.status;
    const detail = err?.error?.message;
    console.log(detail)
    let summary = 'Erro Inesperado';
    let severity = 'error';

    switch (status) {
      case 400:
        summary = 'Requisição Inválida';
        severity = 'warn';
        break;
      case 401: 
        summary = 'Não autorizado';
        severity = 'warn';
        break;
      case 404:
        summary = 'Não Encontrado';
        severity = 'warn';
        break;
      case 409:
        summary = 'Conflito';
        severity = 'warn';
        break;
      case 422:
        summary = 'Erro de Validação';
        severity = 'warn';
        break;
      case 500:
        summary = 'Erro no Servidor';
        severity = 'error';
        break;
    }

    this.msg.add({
      severity: severity,
      summary: summary,
      detail: detail || 'Ocorreu um erro. Tente novamente.'
    });
  }

  
}