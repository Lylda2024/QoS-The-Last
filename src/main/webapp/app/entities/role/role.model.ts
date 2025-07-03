import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';

export interface IRole {
  id: number;
  nom?: string | null;
  description?: string | null;
  utilisateurs?: IUtilisateur[] | null;
}

export type NewRole = Omit<IRole, 'id'> & { id: null };
