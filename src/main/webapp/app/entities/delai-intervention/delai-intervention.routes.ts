// src/main/webapp/app/entities/delai-intervention/delai-intervention.routes.ts
import { Route } from '@angular/router';

import { DelaiInterventionListComponent } from './list/delai-intervention-list.component';
import { DelaiInterventionDetailComponent } from './detail/delai-intervention-detail.component';
import { DelaiInterventionUpdateComponent } from './update/delai-intervention-update.component';
import { delaiInterventionResolve } from './route/delai-intervention-routing-resolve.service';

export const delaiInterventionRoute: Route[] = [
  {
    path: '',
    component: DelaiInterventionListComponent,
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'codeStageApp.delaiIntervention.home.title',
    },
  },
  {
    path: ':id/view',
    component: DelaiInterventionDetailComponent,
    resolve: {
      delaiIntervention: delaiInterventionResolve,
    },
    data: {
      pageTitle: 'codeStageApp.delaiIntervention.home.title',
    },
  },
  {
    path: 'new',
    component: DelaiInterventionUpdateComponent,
    resolve: {
      delaiIntervention: delaiInterventionResolve,
    },
  },
  {
    path: ':id/edit',
    component: DelaiInterventionUpdateComponent,
    resolve: {
      delaiIntervention: delaiInterventionResolve,
    },
  },
];
