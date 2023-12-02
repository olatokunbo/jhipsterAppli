import { IValeur, NewValeur } from './valeur.model';

export const sampleWithRequiredData: IValeur = {
  id: 11038,
};

export const sampleWithPartialData: IValeur = {
  id: 9331,
  code: 'parce que',
  ordre: 10677,
  libelle: 'alimenter mieux',
  description: 'afin que quitte à de façon à ce que',
};

export const sampleWithFullData: IValeur = {
  id: 13853,
  code: 'aux alentours de membre du personnel pendant',
  ordre: 8200,
  libelle: 'collègue main-d’œuvre administration',
  abreviation: 'sans',
  description: 'vaste',
};

export const sampleWithNewData: NewValeur = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
