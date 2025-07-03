import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import HistoriqueResolve from './route/historique-routing-resolve.service';

const historiqueRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/historique.component').then(m => m.HistoriqueComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/historique-detail.component').then(m => m.HistoriqueDetailComponent),
    resolve: {
      historique: HistoriqueResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/historique-update.component').then(m => m.HistoriqueUpdateComponent),
    resolve: {
      historique: HistoriqueResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/historique-update.component').then(m => m.HistoriqueUpdateComponent),
    resolve: {
      historique: HistoriqueResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default historiqueRoute;
