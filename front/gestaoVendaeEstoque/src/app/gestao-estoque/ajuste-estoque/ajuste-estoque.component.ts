import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { RadioButtonModule } from 'primeng/radiobutton';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { ProdutoResponse } from '../../modelos/DTOs/ProdutoDTO';
import { ProdutoService } from '../../service/Produto.Service';
import { finalize } from 'rxjs';

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
export class AjusteEstoqueComponent implements OnInit {

  @Input() produto: ProdutoResponse | null = null;
  @Output() fechar = new EventEmitter();

  tipoMovimentacao: 'ENTRADA' | 'AJUSTE' = 'ENTRADA';
  quantidadeAjuste: number = 1;
  descricaoAjuste: string = '';

  constructor(
    private produtoService: ProdutoService,
    private msg: MessageService
  ) { }

  ngOnInit(): void {
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
      descricao: this.descricaoAjuste
    };

    this.produtoService.loading.set(true);
    // this.produtoService.ajustarEstoque(request).pipe(
    //   finalize(() => this.produtoService.loading.set(false))
    // ).subscribe({
    //   next: () => {
    //     this.msg.add({ severity: 'success', summary: 'Sucesso', detail: 'Estoque atualizado.' });
    //     this.fechar.emit(true); // Emite 'true' para recarregar
    //   },
    //   error: (err) => this.msg.add({ severity: 'error', summary: 'Erro', detail: err.error.message || 'Falha ao ajustar estoque.' })
    // });

    // Simulação
    this.produtoService.loading.set(false);
    this.msg.add({ severity: 'success', summary: 'Sucesso', detail: 'Estoque atualizado (Simulação).' });
    this.fechar.emit();
  }

  cancelar(): void {
    this.fechar.emit();
  }
}