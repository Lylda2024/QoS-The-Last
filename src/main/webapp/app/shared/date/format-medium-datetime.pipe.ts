import { Pipe, PipeTransform } from '@angular/core';
import dayjs, { Dayjs } from 'dayjs/esm';

@Pipe({
  name: 'formatMediumDatetime',
  standalone: true,
})
export class FormatMediumDateTimePipe implements PipeTransform {
  transform(value: Dayjs | null | undefined): string {
    const d = value ? dayjs(value) : null;
    return d?.isValid() ? d.format('D MMM YYYY HH:mm:ss') : '';
  }
}
