import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TypeUtilisateurResolve from './route/type-utilisateur-routing-resolve.service';

const typeUtilisateurRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/type-utilisateur.component').then(m => m.TypeUtilisateurComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/type-utilisateur-detail.component').then(m => m.TypeUtilisateurDetailComponent),
    resolve: {
      typeUtilisateur: TypeUtilisateurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/type-utilisateur-update.component').then(m => m.TypeUtilisateurUpdateComponent),
    resolve: {
      typeUtilisateur: TypeUtilisateurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/type-utilisateur-update.component').then(m => m.TypeUtilisateurUpdateComponent),
    resolve: {
      typeUtilisateur: TypeUtilisateurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default typeUtilisateurRoute;
