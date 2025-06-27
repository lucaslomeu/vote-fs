import { Pauta } from './pauta';

export interface SessaoVotacao {
  id: number;
  pauta: Pauta;
  dataAbertura: string;
  duracaoEmMinutos: number;
  aberta: boolean;
}
