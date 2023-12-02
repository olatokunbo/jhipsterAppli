import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IUniteAdministrative, NewUniteAdministrative } from '../unite-administrative.model';

export type PartialUpdateUniteAdministrative = Partial<IUniteAdministrative> & Pick<IUniteAdministrative, 'id'>;

export type EntityResponseType = HttpResponse<IUniteAdministrative>;
export type EntityArrayResponseType = HttpResponse<IUniteAdministrative[]>;

@Injectable({ providedIn: 'root' })
export class UniteAdministrativeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/unite-administratives');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/unite-administratives/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(uniteAdministrative: NewUniteAdministrative): Observable<EntityResponseType> {
    return this.http.post<IUniteAdministrative>(this.resourceUrl, uniteAdministrative, { observe: 'response' });
  }

  update(uniteAdministrative: IUniteAdministrative): Observable<EntityResponseType> {
    return this.http.put<IUniteAdministrative>(
      `${this.resourceUrl}/${this.getUniteAdministrativeIdentifier(uniteAdministrative)}`,
      uniteAdministrative,
      { observe: 'response' },
    );
  }

  partialUpdate(uniteAdministrative: PartialUpdateUniteAdministrative): Observable<EntityResponseType> {
    return this.http.patch<IUniteAdministrative>(
      `${this.resourceUrl}/${this.getUniteAdministrativeIdentifier(uniteAdministrative)}`,
      uniteAdministrative,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUniteAdministrative>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUniteAdministrative[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUniteAdministrative[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IUniteAdministrative[]>()], asapScheduler)));
  }

  getUniteAdministrativeIdentifier(uniteAdministrative: Pick<IUniteAdministrative, 'id'>): number {
    return uniteAdministrative.id;
  }

  compareUniteAdministrative(o1: Pick<IUniteAdministrative, 'id'> | null, o2: Pick<IUniteAdministrative, 'id'> | null): boolean {
    return o1 && o2 ? this.getUniteAdministrativeIdentifier(o1) === this.getUniteAdministrativeIdentifier(o2) : o1 === o2;
  }

  addUniteAdministrativeToCollectionIfMissing<Type extends Pick<IUniteAdministrative, 'id'>>(
    uniteAdministrativeCollection: Type[],
    ...uniteAdministrativesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const uniteAdministratives: Type[] = uniteAdministrativesToCheck.filter(isPresent);
    if (uniteAdministratives.length > 0) {
      const uniteAdministrativeCollectionIdentifiers = uniteAdministrativeCollection.map(
        uniteAdministrativeItem => this.getUniteAdministrativeIdentifier(uniteAdministrativeItem)!,
      );
      const uniteAdministrativesToAdd = uniteAdministratives.filter(uniteAdministrativeItem => {
        const uniteAdministrativeIdentifier = this.getUniteAdministrativeIdentifier(uniteAdministrativeItem);
        if (uniteAdministrativeCollectionIdentifiers.includes(uniteAdministrativeIdentifier)) {
          return false;
        }
        uniteAdministrativeCollectionIdentifiers.push(uniteAdministrativeIdentifier);
        return true;
      });
      return [...uniteAdministrativesToAdd, ...uniteAdministrativeCollection];
    }
    return uniteAdministrativeCollection;
  }
}
