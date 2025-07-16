// src/main/webapp/app/entities/entity.routes.ts
import { Routes } from '@angular/router';

import authorityRoutes from './admin/authority/authority.routes';
import typeUtilisateurRoutes from './type-utilisateur/type-utilisateur.routes';
import utilisateurRoutes from './utilisateur/utilisateur.routes';
import roleRoutes from './role/role.routes';
import siteRoutes from './site/site.routes';
import degradationRoutes from './degradation/degradation.routes';
import { delaiInterventionRoute } from './delai-intervention/delai-intervention.routes';
import notificationRoutes from './notification/notification.routes';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'codeStageApp.adminAuthority.home.title' },
    children: authorityRoutes,
  },
  {
    path: 'type-utilisateur',
    data: { pageTitle: 'codeStageApp.typeUtilisateur.home.title' },
    children: typeUtilisateurRoutes,
  },
  {
    path: 'utilisateur',
    data: { pageTitle: 'codeStageApp.utilisateur.home.title' },
    children: utilisateurRoutes,
  },
  {
    path: 'role',
    data: { pageTitle: 'codeStageApp.role.home.title' },
    children: roleRoutes,
  },
  {
    path: 'site',
    data: { pageTitle: 'codeStageApp.site.home.title' },
    children: siteRoutes,
  },
  {
    path: 'degradation',
    data: { pageTitle: 'codeStageApp.degradation.home.title' },
    children: degradationRoutes,
  },
  {
    path: 'delai-intervention',
    data: { pageTitle: 'codeStageApp.delaiIntervention.home.title' },
    children: delaiInterventionRoute,
  },
  {
    path: 'notification',
    data: { pageTitle: 'codeStageApp.notification.home.title' },
    children: notificationRoutes,
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
