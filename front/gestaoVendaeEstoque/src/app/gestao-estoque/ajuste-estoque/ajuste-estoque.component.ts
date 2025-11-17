import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { RadioButtonModule } from 'primeng/radiobutton';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { ProdutoResponse } from '../../modelos/DTOs/ProdutoDTO';
import { finalize } from 'rxjs';
import { EstoqueService } from '../../service/Estoque.service';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-ajuste-estoque',
  standalone: true,
  imports: [
    CommonModule, FormsModule, ButtonModule, DialogModule,
    RadioButtonModule, InputNumberModule, InputTextareaModule
  ],
  templateUrl: './ajuste-estoque.component.html',
  styleUrls: ['./ajuste-estoque.component.css']
})
export class AjusteEstoqueComponent implements OnChanges {

  @Input() produto: ProdutoResponse | null = null;
  @Output() fechar = new EventEmitter();

  tipoMovimentacao: 'ENTRADA' | 'AJUSTE' = 'ENTRADA';
  quantidadeAjuste: number = 1;
  descricaoAjuste: string = '';

  constructor(
    private auth: AuthService,
    private estoqueService: EstoqueService,
    private msg: MessageService
  ) { }

  ngOnChanges(): void {
    this.resetarForm();
  }

  private resetarForm(): void {
    this.tipoMovimentacao = 'ENTRADA';
    this.quantidadeAjuste = 1;
    this.descricaoAjuste = '';
  }

  salvar(): void {
    if (!this.produto || !this.quantidadeAjuste || !this.descricaoAjuste) {
      this.msg.add({ severity: 'warn', summary: 'Atenção', detail: 'Preencha todos os campos.' });
      return;
    }

    const request = {
      produtoId: this.produto.id,
      tipo: this.tipoMovimentacao,
      quantidade: this.quantidadeAjuste,
      motivo: this.descricaoAjuste,
      usuarioId: this.auth.getUsuarioLogado()!.id
    };

    this.estoqueService.loading.set(true);


    this.estoqueService.ajustarEstoque(request).pipe(
      finalize(() => this.estoqueService.loading.set(false))
    ).subscribe({
      next: () => {
        this.msg.add({ severity: 'success', summary: 'Sucesso', detail: 'Estoque atualizado.' });
        this.fechar.emit(true);
      },
      error: (err) => this.msg.add({ severity: 'error', summary: 'Erro', detail: err.error.message || 'Falha ao ajustar estoque.' })
    });
  }

  cancelar(): void {
    this.fechar.emit();
  }
  
}