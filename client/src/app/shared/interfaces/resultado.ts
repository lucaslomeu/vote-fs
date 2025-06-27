import { Pauta } from './pauta';

export interface Resultado {
  pauta: Pauta;
  totalSim: number;
  totalNao: number;
  totalVotos: number;
  statusSessao: string;
}
