import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { catchError, of } from 'rxjs';
import { PautaService } from '../../shared/services/pauta.service';
import { Pauta } from '../../shared/interfaces/pauta';

@Component({
  selector: 'app-pauta-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './pauta-form.component.html',
  styleUrls: ['./pauta-form.component.scss'],
})
export class PautaFormComponent {
  isLoading = signal(false);
  errorMessage = signal<string | null>(null);

  private fb = inject(FormBuilder);
  private pautaService = inject(PautaService);
  private router = inject(Router);

  pautaForm: FormGroup = this.fb.group({
    titulo: ['', Validators.required],
    descricao: [''],
  });

  onSubmit(): void {
    if (this.pautaForm.valid) {
      this.isLoading.set(true);
      this.errorMessage.set(null);

      this.pautaService
        .criarPauta(this.pautaForm.value)
        .pipe(
          catchError((error) => {
            this.errorMessage.set(
              error.error?.message || 'Erro ao criar pauta'
            );
            return of(null);
          })
        )
        .subscribe((result: Pauta | null) => {
          this.isLoading.set(false);
          if (result) {
            this.router.navigate(['/pautas']);
          }
        });
    }
  }

  clearError(): void {
    this.errorMessage.set(null);
  }
}
