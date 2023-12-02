import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ValeurComponent } from './list/valeur.component';
import { ValeurDetailComponent } from './detail/valeur-detail.component';
import { ValeurUpdateComponent } from './update/valeur-update.component';
import ValeurResolve from './route/valeur-routing-resolve.service';

const valeurRoute: Routes = [
  {
    path: '',
    component: ValeurComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ValeurDetailComponent,
    resolve: {
      valeur: ValeurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ValeurUpdateComponent,
    resolve: {
      valeur: ValeurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ValeurUpdateComponent,
    resolve: {
      valeur: ValeurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default valeurRoute;
