import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ToolbarModule } from 'primeng/toolbar';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { InputNumberModule } from 'primeng/inputnumber';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { ChartModule } from 'primeng/chart';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { RelatorioService } from '../service/Relatorio.Service';
import { finalize } from 'rxjs';
import { RelatorioDetalhe, RelatorioResponse } from '../modelos/DTOs/RelatorioDTO';
import { UsuarioService } from '../service/UsuarioService';
import { ProdutoService } from '../service/Produto.Service';
import { TotalVendaDTO } from '../modelos/DTOs/graficoDTO';

@Component({
  selector: 'app-relatorio',
  standalone: true,
  imports: [
    CommonModule, FormsModule, ToolbarModule, CalendarModule, DropdownModule,
    InputNumberModule, ButtonModule, CardModule, ChartModule, TableModule, ToastModule
  ],
  templateUrl: './relatorio.component.html',
  styleUrls: ['./relatorio.component.css']
})
export class RelatorioComponent implements OnInit {

  vendas: RelatorioResponse[] = [];
  vendasDetalhas = new Map<number, RelatorioDetalhe>();
  usuariosFiltro: any[] = [];
  produtosFiltro: any[] = [];

  filtroRangeDatas: Date[] | undefined;
  filtroUsuario: any | null = null;
  filtroValorMin: number | null = null;
  filtroValorMax: number | null = null;
  filtroProdutoChart: any | null = null;

  chartTotalVendas: any;
  chartEntradaSaida: any;
  chartVendasOperador: any;
  chartOptions: any;
  primaryColor: string = '#000';
  corLinhaEntrada: string = 'var(--green-500)';
  corLinhaSaida: string = 'var(--red-500)';

  constructor(
    private msg: MessageService,
    private service: RelatorioService,
    private serviceU: UsuarioService,
    private serviceP: ProdutoService
  ) { }

  ngOnInit(): void {
    this.configurarOpcoesChart();
    this.carregarFiltros();
    this.aplicarFiltros();
  }

  carregarFiltros(): void {
    const params: any = {};
    params.perfil = "OPERADOR";
    this.serviceU.listar(params)
      .pipe(finalize(() => this.service.loading.set(false)))
      .subscribe({
        next: (lista) => {
          this.usuariosFiltro = lista.map(u => ({
            label: u.nome,
            value: u.id
          }));
        },
        error: () => {
          this.msg.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao carregar os usuários' });
        }
      });

    this.serviceP.listar()
      .pipe(finalize(() => this.service.loading.set(false)))
      .subscribe({
        next: (lista) => {
          this.produtosFiltro = lista.map(p => ({
            label: p.nome,
            value: p.id
          }));
        },
        error: () => {
          this.msg.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao carregar os usuários' });
        }
      });
  }

  aplicarFiltros(): void {
    const dataInicial = this.filtroRangeDatas?.[0]
      ? this.formatDateOnly(this.filtroRangeDatas[0])
      : undefined;

    const dataFinal = this.filtroRangeDatas?.[1]
      ? this.formatDateOnly(this.filtroRangeDatas[1])
      : undefined;

    const params = {
      dataInicial: dataInicial,
      dataFinal: dataFinal,
      usuarioId: this.filtroUsuario ?? undefined,
      valorMin: this.filtroValorMin ?? undefined,
      valorMax: this.filtroValorMax ?? undefined
    };

    this.service.loading.set(true);

    this.service.listar(params).pipe(
      finalize(() => this.service.loading.set(false))
    ).subscribe({
      next: (listaLeve) => {
        this.vendas = listaLeve;
        this.vendasDetalhas.clear();
        this.atualizarTodosGraficos(this.vendas);
      },
      error: () => {
        this.msg.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao carregar os relatórios.' });
      }
    })
  }

  limparFiltros(): void {
    this.filtroRangeDatas = undefined;
    this.filtroUsuario = null;
    this.filtroValorMin = null;
    this.filtroValorMax = null;
    this.aplicarFiltros();
  }

  onVendaExpand(event: any): void {
    const venda: RelatorioResponse = event.data;
    const vendaId = venda.vendaId;

    this.service.loading.set(true);

    this.service.buscarVendaCompleta(vendaId)
      .pipe(finalize(() => this.service.loading.set(false)))
      .subscribe({
        next: (detalhe: RelatorioDetalhe) => {
          this.vendasDetalhas.clear
          this.vendasDetalhas.set(vendaId, detalhe);
        },
        error: () => {
          this.msg.add({
            severity: 'error',
            summary: 'Erro',
            detail: 'Falha ao buscar detalhes da venda.'
          });
        }
      });
  } 

  private atualizarTodosGraficos(vendas: any[]): void {
    this.configurarOpcoesChart();
    this.atualizarChartTotalVendas();
    this.atualizarChartVendasOperador(vendas);
    this.atualizarChartProduto();
  }

  private atualizarChartTotalVendas(): void {

    const mesesDoAno = [
      'Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun',
      'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'
    ];

    this.service.graficoVendas().subscribe({
      next: (listaLeve) => {

        const dadosDoGrafico = new Array(12).fill(0);

        for (const item of listaLeve) {
          const mesIndex = item.mes - 1;
          if (mesIndex >= 0 && mesIndex < 12) {
            dadosDoGrafico[mesIndex] = item.totalVendido;
          }
        }

        this.chartTotalVendas = {
          labels: mesesDoAno,
          datasets: [
            {
              label: 'Total de Vendas (R$)',
              backgroundColor: this.primaryColor,
              data: dadosDoGrafico
            }
          ]
        };

        this.atualizarTodosGraficos(this.vendas);

      },
      error: () => {
        this.msg.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao carregar os relatórios.' });
      }
    });

  }

  atualizarChartProduto(): void {

    this.chartEntradaSaida = {
      labels: ['Seg', 'Ter', 'Qua', 'Qui', 'Sex'],
      datasets: [
        {
          label: 'Entradas',
          data: [10, 15, 0, 5, 20],
          fill: false,
          borderColor: this.corLinhaEntrada,
          tension: 0.4
        },
        {
          label: 'Saídas (Vendas)',
          data: [5, 8, 3, 7, 10],
          fill: false,
          borderColor: this.corLinhaSaida,
          tension: 0.4
        }
      ]
    };
  }

  private atualizarChartVendasOperador(vendas: RelatorioResponse[]): void {

    const contagemPorOperador = new Map<string, number>();

    for (const venda of vendas) {
      const nomeOperador = venda.nome || 'Desconhecido';

      const contagemAtual = contagemPorOperador.get(nomeOperador) || 0;
      contagemPorOperador.set(nomeOperador, contagemAtual + 1);
    }

    const labels = Array.from(contagemPorOperador.keys());
    const data = Array.from(contagemPorOperador.values());

    this.chartVendasOperador = {
      labels: labels,
      datasets: [
        {
          label: 'Nº de Vendas',
          backgroundColor: this.primaryColor,
          data: data
        }
      ]
    };
  }

  private configurarOpcoesChart(): void {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

    this.primaryColor = documentStyle.getPropertyValue('--primary-color').trim();
    this.corLinhaEntrada = documentStyle.getPropertyValue('--green-500').trim();
    this.corLinhaSaida = documentStyle.getPropertyValue('--red-500').trim();

    this.chartOptions = {
      plugins: { legend: { labels: { color: textColor } } },
      scales: {
        y: { ticks: { color: textColorSecondary }, grid: { color: surfaceBorder } },
        x: { ticks: { color: textColorSecondary }, grid: { color: surfaceBorder } }
      }
    };
  }

  private formatDateOnly(date: Date | undefined): string | undefined {
    if (!date) return undefined;

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
  }
}