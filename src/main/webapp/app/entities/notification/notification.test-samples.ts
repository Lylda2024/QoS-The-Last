import dayjs from 'dayjs/esm';

import { INotification, NewNotification } from './notification.model';

export const sampleWithRequiredData: INotification = {
  id: 10110,
};

export const sampleWithPartialData: INotification = {
  id: 29,
  dateEnvoi: dayjs('2025-06-19T15:39'),
};

export const sampleWithFullData: INotification = {
  id: 5787,
  type: 'sédentaire émérite',
  message: 'avare',
  dateEnvoi: dayjs('2025-06-20T02:18'),
  statutLecture: true,
};

export const sampleWithNewData: NewNotification = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
