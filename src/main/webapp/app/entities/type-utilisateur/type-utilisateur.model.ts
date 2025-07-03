export interface ITypeUtilisateur {
  id: number;
  nom?: string | null;
  description?: string | null;
  niveau?: number | null;
  permissions?: string | null;
}

export type NewTypeUtilisateur = Omit<ITypeUtilisateur, 'id'> & { id: null };
