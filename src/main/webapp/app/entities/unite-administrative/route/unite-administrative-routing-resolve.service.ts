import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUniteAdministrative } from '../unite-administrative.model';
import { UniteAdministrativeService } from '../service/unite-administrative.service';

export const uniteAdministrativeResolve = (route: ActivatedRouteSnapshot): Observable<null | IUniteAdministrative> => {
  const id = route.params['id'];
  if (id) {
    return inject(UniteAdministrativeService)
      .find(id)
      .pipe(
        mergeMap((uniteAdministrative: HttpResponse<IUniteAdministrative>) => {
          if (uniteAdministrative.body) {
            return of(uniteAdministrative.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default uniteAdministrativeResolve;
