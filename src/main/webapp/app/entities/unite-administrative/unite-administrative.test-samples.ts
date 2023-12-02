import { IUniteAdministrative, NewUniteAdministrative } from './unite-administrative.model';

export const sampleWithRequiredData: IUniteAdministrative = {
  id: 7479,
};

export const sampleWithPartialData: IUniteAdministrative = {
  id: 20055,
  ordre: 5399,
};

export const sampleWithFullData: IUniteAdministrative = {
  id: 8035,
  code: 'hirsute absolument sombre',
  ordre: 24609,
  libelle: 'areu areu en orange',
};

export const sampleWithNewData: NewUniteAdministrative = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
