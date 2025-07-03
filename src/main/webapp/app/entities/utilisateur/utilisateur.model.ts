// src/main/webapp/app/entities/utilisateur/utilisateur.model.ts
import { ITypeUtilisateur } from 'app/entities/type-utilisateur/type-utilisateur.model';
import { IRole } from 'app/entities/role/role.model';

export interface IUtilisateur {
  id: number;
  login?: string | null; // ✅ ajouté pour éviter l'erreur
  nom?: string | null;
  prenom?: string | null;
  email?: string | null;
  motDePasse?: string | null;
  typeUtilisateur?: ITypeUtilisateur | null;
  roles?: IRole[] | null;
}

export type NewUtilisateur = Omit<IUtilisateur, 'id'> & { id: null };
