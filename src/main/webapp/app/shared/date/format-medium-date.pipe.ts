import { Pipe, PipeTransform } from '@angular/core';
import dayjs, { Dayjs } from 'dayjs/esm';

@Pipe({
  name: 'formatMediumDate',
  standalone: true,
})
export class FormatMediumDatePipe implements PipeTransform {
  transform(day: Dayjs | null | undefined): string {
    return day ? day.format('D MMM YYYY') : '';
  }
}
