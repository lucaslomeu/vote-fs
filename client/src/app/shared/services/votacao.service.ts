import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SessaoVotacao } from '../interfaces/sessao';
import { Voto } from '../interfaces/voto';
import { Resultado } from '../interfaces/resultado';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environments';

@Injectable({
  providedIn: 'root',
})
export class VotacaoService {
  private readonly API_BASE = environment.apiUrl + '/votacao';
  protected http = inject(HttpClient);
  protected router = inject(Router);

  abrirSessao(sessao: {
    pautaId: number;
    duracaoEmMinutos?: number;
  }): Observable<SessaoVotacao> {
    return this.http.post<SessaoVotacao>(
      `${this.API_BASE}/sessoes/abrir`,
      sessao
    );
  }

  votar(
    pautaId: number,
    voto: { idAssociado: string; opcaoVoto: string }
  ): Observable<Voto> {
    return this.http.post<Voto>(
      `${this.API_BASE}/pautas/${pautaId}/votos`,
      voto
    );
  }

  obterResultado(pautaId: number): Observable<Resultado> {
    return this.http.get<Resultado>(
      `${this.API_BASE}/pautas/${pautaId}/resultado`
    );
  }
}
