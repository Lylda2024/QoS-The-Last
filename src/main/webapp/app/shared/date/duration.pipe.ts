import { Pipe, PipeTransform } from '@angular/core';
import dayjs from 'dayjs/esm';
import duration from 'dayjs/esm/plugin/duration';

dayjs.extend(duration);

@Pipe({
  name: 'duration',
  standalone: true,
})
export class DurationPipe implements PipeTransform {
  transform(value: any): string {
    if (value) {
      return dayjs.duration(value).humanize();
    }
    return '';
  }
}
