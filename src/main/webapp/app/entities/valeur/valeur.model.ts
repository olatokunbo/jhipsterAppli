export interface IValeur {
  id: number;
  code?: string | null;
  ordre?: number | null;
  libelle?: string | null;
  abreviation?: string | null;
  description?: string | null;
  valeurParent?: Pick<IValeur, 'id'> | null;
}

export type NewValeur = Omit<IValeur, 'id'> & { id: null };
