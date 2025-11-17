import { HttpClient } from "@angular/common/http";
import { Injectable, signal } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { environment } from "../../environments/environment";
import { ProdutoResponse } from "../modelos/DTOs/ProdutoDTO";
import { VendaReponse, VendaRequest } from "../modelos/DTOs/VendaDTOs";


@Injectable({ providedIn: "root" })

export class VendaService {

    private readonly UrlVenda = `${environment.apiUrl}/vendas`;
    private readonly Url = `${environment.apiUrl}/itens-venda`;

    loading = signal(false);
    private _cache$ = new BehaviorSubject<ProdutoResponse[] | null>(null);
    cache$ = this._cache$.asObservable();

    constructor(private http: HttpClient) { }

    criar(req: VendaRequest): Observable<VendaReponse> {
        return this.http.post<VendaReponse>(this.UrlVenda, req);
    }
}