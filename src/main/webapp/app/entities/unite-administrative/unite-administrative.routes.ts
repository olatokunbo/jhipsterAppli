import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { UniteAdministrativeComponent } from './list/unite-administrative.component';
import { UniteAdministrativeDetailComponent } from './detail/unite-administrative-detail.component';
import { UniteAdministrativeUpdateComponent } from './update/unite-administrative-update.component';
import UniteAdministrativeResolve from './route/unite-administrative-routing-resolve.service';

const uniteAdministrativeRoute: Routes = [
  {
    path: '',
    component: UniteAdministrativeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UniteAdministrativeDetailComponent,
    resolve: {
      uniteAdministrative: UniteAdministrativeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UniteAdministrativeUpdateComponent,
    resolve: {
      uniteAdministrative: UniteAdministrativeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UniteAdministrativeUpdateComponent,
    resolve: {
      uniteAdministrative: UniteAdministrativeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default uniteAdministrativeRoute;
