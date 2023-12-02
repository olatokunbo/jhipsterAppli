import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IValeur, NewValeur } from '../valeur.model';

export type PartialUpdateValeur = Partial<IValeur> & Pick<IValeur, 'id'>;

export type EntityResponseType = HttpResponse<IValeur>;
export type EntityArrayResponseType = HttpResponse<IValeur[]>;

@Injectable({ providedIn: 'root' })
export class ValeurService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/valeurs');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/valeurs/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(valeur: NewValeur): Observable<EntityResponseType> {
    return this.http.post<IValeur>(this.resourceUrl, valeur, { observe: 'response' });
  }

  update(valeur: IValeur): Observable<EntityResponseType> {
    return this.http.put<IValeur>(`${this.resourceUrl}/${this.getValeurIdentifier(valeur)}`, valeur, { observe: 'response' });
  }

  partialUpdate(valeur: PartialUpdateValeur): Observable<EntityResponseType> {
    return this.http.patch<IValeur>(`${this.resourceUrl}/${this.getValeurIdentifier(valeur)}`, valeur, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IValeur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IValeur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IValeur[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IValeur[]>()], asapScheduler)));
  }

  getValeurIdentifier(valeur: Pick<IValeur, 'id'>): number {
    return valeur.id;
  }

  compareValeur(o1: Pick<IValeur, 'id'> | null, o2: Pick<IValeur, 'id'> | null): boolean {
    return o1 && o2 ? this.getValeurIdentifier(o1) === this.getValeurIdentifier(o2) : o1 === o2;
  }

  addValeurToCollectionIfMissing<Type extends Pick<IValeur, 'id'>>(
    valeurCollection: Type[],
    ...valeursToCheck: (Type | null | undefined)[]
  ): Type[] {
    const valeurs: Type[] = valeursToCheck.filter(isPresent);
    if (valeurs.length > 0) {
      const valeurCollectionIdentifiers = valeurCollection.map(valeurItem => this.getValeurIdentifier(valeurItem)!);
      const valeursToAdd = valeurs.filter(valeurItem => {
        const valeurIdentifier = this.getValeurIdentifier(valeurItem);
        if (valeurCollectionIdentifiers.includes(valeurIdentifier)) {
          return false;
        }
        valeurCollectionIdentifiers.push(valeurIdentifier);
        return true;
      });
      return [...valeursToAdd, ...valeurCollection];
    }
    return valeurCollection;
  }
}
