import { HttpClient } from "@angular/common/http";
import { Injectable, signal } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { environment } from "../../environments/environment";
import { ProdutoResponse } from "../modelos/DTOs/ProdutoDTO";
import { MovimentacaoEstoqueResquet } from "../modelos/DTOs/MovimentacaoEstoqueDTO";

@Injectable({ providedIn: "root" })

export class EstoqueService {

    private readonly baseUrl = `${environment.apiUrl}/movimentacoes`;

    loading = signal(false);
    private _cache$ = new BehaviorSubject<ProdutoResponse[] | null>(null);
    cache$ = this._cache$.asObservable();

    constructor(private http: HttpClient) { }

    ajustarEstoque(req: MovimentacaoEstoqueResquet): Observable<MovimentacaoEstoqueResquet> {
        return this.http.post<MovimentacaoEstoqueResquet>(this.baseUrl, req);
    }
}