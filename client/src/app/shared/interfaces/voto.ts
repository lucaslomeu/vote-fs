import { OpcaoVoto } from './opcao';

export interface Voto {
  id: number;
  idAssociado: string;
  opcaoVoto: OpcaoVoto;
}
