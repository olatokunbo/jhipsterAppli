export interface IValeur {
  id: number;
  code?: string | null;
  ordre?: number | null;
  libelle?: string | null;
  abreviation?: string | null;
  description?: string | null;
  valeurListFils?: Pick<IValeur, 'id'> | null;
}

export type NewValeur = Omit<IValeur, 'id'> & { id: null };
