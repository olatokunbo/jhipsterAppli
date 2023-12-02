import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IValeur } from '../valeur.model';
import { ValeurService } from '../service/valeur.service';

export const valeurResolve = (route: ActivatedRouteSnapshot): Observable<null | IValeur> => {
  const id = route.params['id'];
  if (id) {
    return inject(ValeurService)
      .find(id)
      .pipe(
        mergeMap((valeur: HttpResponse<IValeur>) => {
          if (valeur.body) {
            return of(valeur.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default valeurResolve;
