import dayjs from 'dayjs/esm';
import { IDegradation } from 'app/entities/degradation/degradation.model';

export interface INotification {
  id: number | null;
  type?: string | null;
  message?: string | null;
  dateEnvoi?: dayjs.Dayjs | null;
  statutLecture?: boolean | null;
  degradation?: IDegradation | null;
}

export type NewNotification = Omit<INotification, 'id'> & { id: null };
