import { Routes } from '@angular/router';

import { Authority } from 'app/config/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { errorRoute } from './layouts/error/error.route';

// 🔁 Importation des routes entités
import ENTITY_ROUTES from './entities/entity.routes';

const routes: Routes = [
  // ➤ Redirection initiale vers /home
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'home',
  },

  // ➤ Navbar
  {
    path: '',
    loadComponent: () => import('./layouts/navbar/navbar.component'),
    outlet: 'navbar',
  },

  // ➤ Composant Home (accueil)
  {
    path: 'home',
    loadComponent: () => import('./home/home.component'),
    canActivate: [UserRouteAccessService],
  },

  // ➤ Login
  {
    path: 'login',
    loadComponent: () => import('./login/login.component'),
    title: 'login.title',
  },

  // ➤ Compte utilisateur
  {
    path: 'account',
    loadChildren: () => import('./account/account.route'),
  },

  // ➤ Admin
  {
    path: 'admin',
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
    loadChildren: () => import('./admin/admin.routes'),
  },

  // ➤ Routes entités métier (inclut délai)
  {
    path: '',
    children: ENTITY_ROUTES,
  },

  // ➤ Gestion des erreurs
  ...errorRoute,
];

export default routes;
