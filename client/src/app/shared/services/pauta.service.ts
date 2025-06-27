import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Pauta } from '../interfaces/pauta';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environments';

@Injectable({
  providedIn: 'root',
})
export class PautaService {
  private readonly API_BASE = environment.apiUrl + '/pautas';
  protected http = inject(HttpClient);
  protected router = inject(Router);

  criarPauta(pauta: { titulo: string; descricao: string }): Observable<Pauta> {
    return this.http.post<Pauta>(`${this.API_BASE}`, pauta);
  }

  listarPautas(): Observable<Pauta[]> {
    return this.http.get<Pauta[]>(`${this.API_BASE}`);
  }

  buscarPautaPorId(id: number): Observable<Pauta> {
    return this.http.get<Pauta>(`${this.API_BASE}/${id}`);
  }
}
