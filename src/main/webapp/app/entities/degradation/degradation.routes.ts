import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DegradationResolve from './route/degradation-routing-resolve.service';

const degradationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/degradation.component').then(m => m.DegradationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/degradation-detail.component').then(m => m.DegradationDetailComponent),
    resolve: {
      degradation: DegradationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/degradation-update.component').then(m => m.DegradationUpdateComponent),
    resolve: {
      degradation: DegradationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/degradation-update.component').then(m => m.DegradationUpdateComponent),
    resolve: {
      degradation: DegradationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default degradationRoute;
