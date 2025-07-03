import dayjs from 'dayjs/esm';
import { IDegradation } from 'app/entities/degradation/degradation.model';

export interface IHistorique {
  id: number;
  utilisateur?: string | null;
  section?: string | null;
  horodatage?: dayjs.Dayjs | null;
  degradation?: IDegradation | null;
}

export type NewHistorique = Omit<IHistorique, 'id'> & { id: null };
