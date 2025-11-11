import { Injectable, signal } from "@angular/core";
import { environment } from "../../environments/environment";
import { BehaviorSubject, Observable } from "rxjs";
import { HttpClient, HttpParams } from "@angular/common/http";
import { ProdutoResponse, ProdutoRequest } from "../modelos/DTOs/ProdutoDTO";
import { UsuarioLogin } from "../modelos/DTOs/UsuarioDTOs";

@Injectable({providedIn: "root"})

export class ProdutoService {

    private readonly baseUrl = `${environment.apiUrl}/produtos`;
    
    loading = signal(false);
    private _cache$ = new BehaviorSubject<ProdutoResponse[] | null>(null);
    cache$ = this._cache$.asObservable();
    
    constructor(private http: HttpClient) {}
    
    listar(params?: { filtro?: string; perfil?: string; ativo?: boolean }): Observable<ProdutoResponse[]> {
        let httpParams = new HttpParams();

        if (params?.perfil) httpParams = httpParams.set('perfil', params.perfil);
        if (params?.ativo !== undefined && params?.ativo !== null) {httpParams = httpParams.set('ativo', params.ativo)};
        
        return this.http.get<ProdutoResponse[]>(this.baseUrl, { params: httpParams });
    }
    
    buscarPorId(id: number): Observable<ProdutoResponse> {
        return this.http.get<ProdutoResponse>(`${this.baseUrl}/${id}`);
    }
    
    criar(req: ProdutoRequest): Observable<ProdutoResponse> {
        return this.http.post<ProdutoResponse>(this.baseUrl, req);
    }
    
    atualizar(id: number, req: ProdutoRequest): Observable<ProdutoResponse> {
        return this.http.put<ProdutoResponse>(`${this.baseUrl}/${id}`, req);
    }
    
    inativar(id: number): Observable<ProdutoResponse> {
        return this.http.patch<ProdutoResponse>(`${this.baseUrl}/${id}/inativar`, {});
    }
    
    login(req: UsuarioLogin): Observable<ProdutoResponse> {
        return this.http.post<ProdutoResponse>(`${this.baseUrl}/login`, req);
    }
    
    setCache(list: ProdutoResponse[]) { this._cache$.next(list); }
    clearCache() { this._cache$.next(null); }
    
}
    
