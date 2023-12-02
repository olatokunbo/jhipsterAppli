import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'valeur',
        data: { pageTitle: 'jhipsterAppliApp.valeur.home.title' },
        loadChildren: () => import('./valeur/valeur.routes'),
      },
      {
        path: 'unite-administrative',
        data: { pageTitle: 'jhipsterAppliApp.uniteAdministrative.home.title' },
        loadChildren: () => import('./unite-administrative/unite-administrative.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
