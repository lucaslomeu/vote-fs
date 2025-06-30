import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { PautaService } from '../../shared/services/pauta.service';
import { VotacaoService } from '../../shared/services/votacao.service';
import { Pauta } from '../../shared/interfaces/pauta';
import { Resultado } from '../../shared/interfaces/resultado';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-pauta-detail',
  standalone: true,
  templateUrl: './pauta-detail.component.html',
  styleUrls: ['./pauta-detail.component.scss'],
  imports: [CommonModule, ReactiveFormsModule],
})
export class PautaDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private pautaService = inject(PautaService);
  private votacaoService = inject(VotacaoService);
  private fb = inject(FormBuilder);

  pauta = signal<Pauta | null>(null);
  resultado = signal<Resultado | null>(null);
  errorMessage = signal<string | null>(null);
  isLoading = signal(false);
  pautaId: number = 0;

  sessaoForm: FormGroup = this.fb.group({
    duracaoEmMinutos: [1, [Validators.required, Validators.min(1)]],
  });
  votacaoForm: FormGroup = this.fb.group({
    idAssociado: ['', Validators.required],
    opcaoVoto: ['', Validators.required],
  });

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.pautaId = Number(idParam);
    if (!this.pautaId) {
      this.errorMessage.set('ID de pauta inválido.');
      return;
    }
    this.carregarPauta();
    this.carregarResultado();
  }

  carregarPauta() {
    this.pautaService.buscarPautaPorId(this.pautaId).subscribe({
      next: (pauta) => this.pauta.set(pauta),
      error: () => this.errorMessage.set('Erro ao carregar pauta'),
    });
  }

  carregarResultado() {
    if (this.pauta()) {
      this.votacaoService.obterResultado(this.pautaId).subscribe({
        next: (resultado) => this.resultado.set(resultado),
        error: (err) => {
          console.warn('err', err);
          if (err?.status !== 404) {
            this.errorMessage.set('Erro ao carregar resultado');
          } else {
            this.resultado.set(null);
          }
        },
      });
    }
  }

  abrirSessao() {
    const duracao = this.sessaoForm.value.duracaoEmMinutos;
    this.errorMessage.set(null);
    this.isLoading.set(true);
    this.votacaoService
      .abrirSessao({ pautaId: this.pautaId, duracaoEmMinutos: duracao })
      .subscribe({
        next: () => {
          this.carregarResultado();
          this.isLoading.set(false);
        },
        error: (err) => {
          this.errorMessage.set(
            err?.error?.message || 'Erro ao abrir sessão de votação'
          );
          this.isLoading.set(false);
        },
      });
  }

  votar() {
    if (
      !this.votacaoForm.value.idAssociado ||
      this.votacaoForm.value.idAssociado.trim() === ''
    ) {
      this.errorMessage.set(
        'Por favor, preencha o ID do associado para votar.'
      );
      return;
    }
    if (this.votacaoForm.invalid) return;
    this.errorMessage.set(null);
    this.isLoading.set(true);
    this.votacaoService.votar(this.pautaId, this.votacaoForm.value).subscribe({
      next: () => {
        this.carregarResultado();
        this.votacaoForm.reset();
        this.isLoading.set(false);
      },
      error: (err) => {
        this.errorMessage.set(err?.error?.message || 'Erro ao votar');
        this.isLoading.set(false);
      },
    });
  }

  clearError(): void {
    this.errorMessage.set(null);
  }
}
