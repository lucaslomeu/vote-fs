import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
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
  pauta: Pauta | null = null;
  resultado: Resultado | null = null;
  error: string | null = null;
  pautaId: number = 0;

  sessaoForm: FormGroup;
  votacaoForm: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private pautaService: PautaService,
    private votacaoService: VotacaoService,
    private fb: FormBuilder
  ) {
    this.sessaoForm = this.fb.group({
      duracaoEmMinutos: [1, [Validators.required, Validators.min(1)]],
    });
    this.votacaoForm = this.fb.group({
      idAssociado: ['', Validators.required],
      opcaoVoto: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.pautaId = Number(idParam);
    if (!this.pautaId) {
      this.error = 'ID de pauta inválido.';
      return;
    }
    this.carregarPauta();
    this.carregarResultado();
  }

  carregarPauta() {
    this.pautaService.buscarPautaPorId(this.pautaId).subscribe({
      next: (pauta) => (this.pauta = pauta),
      error: () => (this.error = 'Erro ao carregar pauta'),
    });
  }

  carregarResultado() {
    this.votacaoService.obterResultado(this.pautaId).subscribe({
      next: (resultado) => (this.resultado = resultado),
      error: () => (this.error = 'Erro ao carregar resultado'),
    });
  }

  abrirSessao() {
    const duracao = this.sessaoForm.value.duracaoEmMinutos;
    this.error = null;
    this.votacaoService
      .abrirSessao({ pautaId: this.pautaId, duracaoEmMinutos: duracao })
      .subscribe({
        next: () => this.carregarResultado(),
        error: (err) =>
        (this.error =
          err?.error?.message || 'Erro ao abrir sessão de votação'),
      });
  }

  votar() {
    if (this.votacaoForm.invalid) return;
    this.error = null;
    this.votacaoService
      .votar(this.pautaId, this.votacaoForm.value)
      .subscribe({
        next: () => {
          this.carregarResultado();
          this.votacaoForm.reset();
        },
        error: (err) =>
          (this.error = err?.error?.message || 'Erro ao votar'),
      });
  }
}