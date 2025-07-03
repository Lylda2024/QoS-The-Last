import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SiteResolve from './route/site-routing-resolve.service';

const siteRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/site.component').then(m => m.SiteComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/site-detail.component').then(m => m.SiteDetailComponent),
    resolve: {
      site: SiteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/site-update.component').then(m => m.SiteUpdateComponent),
    resolve: {
      site: SiteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/site-update.component').then(m => m.SiteUpdateComponent),
    resolve: {
      site: SiteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default siteRoute;
