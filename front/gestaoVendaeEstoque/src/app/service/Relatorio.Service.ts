import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable, signal } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { environment } from "../../environments/environment";
import { RelatorioDetalhe, RelatorioResponse } from "../modelos/DTOs/RelatorioDTO";
import { totalProdutoDTO, TotalVendaDTO } from "../modelos/DTOs/graficoDTO";

@Injectable({ providedIn: "root" })

export class RelatorioService {

    private readonly baseUrl = `${environment.apiUrl}/relatorios`;

    loading = signal(false);
    private _cache$ = new BehaviorSubject<RelatorioResponse[] | null>(null);
    cache$ = this._cache$.asObservable();

    constructor(private http: HttpClient) { }

    listar(params?: {
        dataInicial?: string;
        dataFinal?: string;
        usuarioId?: number;
        valorMin?: number;
        valorMax?: number;
    }): Observable<RelatorioResponse[]> {

        let httpParams = new HttpParams();

        if (params?.dataInicial) httpParams = httpParams.set('dataInicial', params.dataInicial);
        if (params?.dataFinal) httpParams = httpParams.set('dataFinal', params.dataFinal);
        if (params?.usuarioId) httpParams = httpParams.set('usuarioId', params.usuarioId.toString());
        if (params?.valorMin !== null && params?.valorMin !== undefined)
            httpParams = httpParams.set('valorMin', params.valorMin.toString());
        if (params?.valorMax !== null && params?.valorMax !== undefined)
            httpParams = httpParams.set('valorMax', params.valorMax.toString());

        return this.http.get<RelatorioResponse[]>(`${this.baseUrl}/resumo`, { params: httpParams });
    }

    buscarVendaCompleta(id: number): Observable<RelatorioDetalhe> {
        return this.http.get<RelatorioDetalhe>(`${this.baseUrl}/venda/${id}`);
    }

    graficoVendas(): Observable<TotalVendaDTO[]> {
        return this.http.get<TotalVendaDTO[]>(`${this.baseUrl}/vendas/por-mes`);
    }

    graficoProduto(id: number): Observable<totalProdutoDTO[]> {
        return this.http.get<totalProdutoDTO[]>(`${this.baseUrl}/movimentacoes/produto/${id}`);
    }
}