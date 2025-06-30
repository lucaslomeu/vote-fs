import { Pauta } from './pauta';

export interface Resultado {
  statusSessao: 'Aberta' | 'Fechada';
  totalSim: number;
  totalNao: number;
  totalVotos: number;
}

export interface PautaListItem extends Pauta {
  aberta: boolean;
  totalVotos: number;
  sessaoAberta: boolean;
}
