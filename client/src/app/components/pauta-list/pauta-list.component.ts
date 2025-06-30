import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { PautaService } from '../../shared/services/pauta.service';
import { toSignal } from '@angular/core/rxjs-interop';
import { catchError, of, map } from 'rxjs';
import {
  PautaListItem,
  Resultado,
} from '../../shared/interfaces/pauta-list-item';

@Component({
  selector: 'app-pauta-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './pauta-list.component.html',
  styleUrls: ['./pauta-list.component.scss'],
})
export class PautaListComponent {
  private pautaService = inject(PautaService);
  private router = inject(Router);

  errorMessage = signal<string | null>(null);

  pautas = toSignal(
    this.pautaService.listarPautas().pipe(
      map((pautas) =>
        pautas.map((pauta: any) => {
          console.warn('pauta, pauta', pauta);
          const resultado: Resultado | undefined = pauta.resultado;
          return {
            ...pauta,
            aberta: resultado?.statusSessao === 'Aberta',
            totalVotos: resultado?.totalVotos ?? 0,
          } as PautaListItem;
        })
      ),
      catchError((error) => {
        this.errorMessage.set('Erro ao carregar pautas');
        return of([]);
      })
    ),
    { initialValue: [] as PautaListItem[] }
  );

  hasPautas = computed(() => this.pautas().length > 0);
  hasError = computed(() => this.errorMessage() !== null);

  openPauta(pautaId: number): void {
    this.router.navigate(['/pautas', pautaId]);
  }

  clearError(): void {
    this.errorMessage.set(null);
  }
}
