import { Routes } from '@angular/router';

import { Authority } from 'app/config/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { errorRoute } from './layouts/error/error.route';

const routes: Routes = [
  // ➤ Redirection initiale vers /home
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'home',
  },

  // ➤ Navbar dans outlet
  {
    path: '',
    loadComponent: () => import('./layouts/navbar/navbar.component'),
    outlet: 'navbar',
  },

  // ➤ Composant Home (page d’accueil sécurisée)
  {
    path: 'home',
    loadComponent: () => import('./home/home.component'),
    canActivate: [UserRouteAccessService],
  },

  // ➤ Composant Login
  {
    path: 'login',
    loadComponent: () => import('./login/login.component'),
    title: 'login.title',
  },

  // ➤ Compte utilisateur (register, settings, etc.)
  {
    path: 'account',
    loadChildren: () => import('./account/account.route'),
  },

  // ➤ Admin sécurisé (pour les ADMIN uniquement)
  {
    path: 'admin',
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
    loadChildren: () => import('./admin/admin.routes'),
  },

  // ➤ Routes pour les entités métier (ex : /historique, /degradation, etc.)
  {
    path: '',
    loadChildren: () => import('./entities/entity.routes'),
  },

  // ➤ Gestion des erreurs (404, etc.)
  ...errorRoute,
];

export default routes;
