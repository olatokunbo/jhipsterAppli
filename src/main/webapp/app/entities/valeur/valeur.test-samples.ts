import { IValeur, NewValeur } from './valeur.model';

export const sampleWithRequiredData: IValeur = {
  id: 31021,
};

export const sampleWithPartialData: IValeur = {
  id: 20711,
  code: 'à même',
  abreviation: 'électorat accuser',
  description: 'approximativement préparer',
};

export const sampleWithFullData: IValeur = {
  id: 1622,
  code: 'drelin charitable ha',
  ordre: 22531,
  libelle: 'exiger au cas où rectorat',
  abreviation: 'désagréable sale malgré',
  description: 'alors que paraître',
};

export const sampleWithNewData: NewValeur = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
