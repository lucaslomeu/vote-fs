import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { PautaService } from '../../shared/services/pauta.service';
import { toSignal } from '@angular/core/rxjs-interop';
import { catchError, of } from 'rxjs';

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
      catchError((error) => {
        this.errorMessage.set('Erro ao carregar pautas');
        return of([]);
      })
    ),
    { initialValue: [] }
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
