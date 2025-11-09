import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable, signal } from "@angular/core";
import { environment } from "../../environments/environment";
import { BehaviorSubject, Observable } from "rxjs";
import { UsuarioResponse, UsuarioRequest, UsuarioLogin } from "../modelos/DTOs/UsuarioDTOs";

@Injectable({providedIn: "root"})

export class UsuarioService {

    private readonly baseUrl = `${environment.apiUrl}/usuarios`;

    loading = signal(false);
    private _cache$ = new BehaviorSubject<UsuarioResponse[] | null>(null);
    cache$ = this._cache$.asObservable();

    constructor(private http: HttpClient) {}

    listar(params?: { perfil?: string; ativo?: boolean }): Observable<UsuarioResponse[]> {
    let httpParams = new HttpParams();
    if (params?.perfil) httpParams = httpParams.set('perfil', params.perfil);
    if (params?.ativo !== undefined && params?.ativo !== null) {
      httpParams = httpParams.set('ativo', params.ativo);
    }
    return this.http.get<UsuarioResponse[]>(this.baseUrl, { params: httpParams });
  }

  buscarPorId(id: number): Observable<UsuarioResponse> {
    return this.http.get<UsuarioResponse>(`${this.baseUrl}/${id}`);
  }

  criar(req: UsuarioRequest): Observable<UsuarioResponse> {
    return this.http.post<UsuarioResponse>(this.baseUrl, req);
  }

  atualizar(id: number, req: UsuarioRequest): Observable<UsuarioResponse> {
    return this.http.put<UsuarioResponse>(`${this.baseUrl}/${id}`, req);
  }

  inativar(id: number): Observable<UsuarioResponse> {
    return this.http.patch<UsuarioResponse>(`${this.baseUrl}/${id}/inativar`, {});
  }

  login(req: UsuarioLogin): Observable<UsuarioResponse> {
    return this.http.post<UsuarioResponse>(`${this.baseUrl}/login`, req);
  }

  setCache(list: UsuarioResponse[]) { this._cache$.next(list); }
  clearCache() { this._cache$.next(null); }

}
