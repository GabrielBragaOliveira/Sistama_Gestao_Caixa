import { HttpClient } from "@angular/common/http";
import { Injectable, signal } from "@angular/core";
import { BehaviorSubject } from "rxjs";
import { environment } from "../../environments/environment";
import { ProdutoResponse } from "../modelos/DTOs/ProdutoDTO";

@Injectable({providedIn: "root"})

export class RelatorioService {
    
    private readonly baseUrl = `${environment.apiUrl}/relatorios`;
    
    loading = signal(false);
    private _cache$ = new BehaviorSubject<ProdutoResponse[] | null>(null);
    cache$ = this._cache$.asObservable();
    
    constructor(private http: HttpClient) {}
}