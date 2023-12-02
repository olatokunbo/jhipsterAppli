import { IValeur } from 'app/entities/valeur/valeur.model';

export interface IUniteAdministrative {
  id: number;
  code?: string | null;
  ordre?: number | null;
  libelle?: string | null;
  typeUniteAdministrative?: Pick<IValeur, 'id'> | null;
  uniteAdministrativeParent?: Pick<IUniteAdministrative, 'id'> | null;
}

export type NewUniteAdministrative = Omit<IUniteAdministrative, 'id'> & { id: null };
