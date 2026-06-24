import { Component, input, output, AfterViewInit, ViewEncapsulation } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface DistrictData { code: string; name: string; nameTamil: string; value: number; }

@Component({
  selector: 'avgc-district-map',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './district-map.component.html',
  styleUrls: ['./district-map.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class DistrictMapComponent implements AfterViewInit {
  districts = input<DistrictData[]>([]);
  height = input<string>('500px');
  districtSelected = output<DistrictData>();

  ngAfterViewInit(): void {
    // Tamil Nadu district map renders via SVG
    // Color intensity mapped to value using primary palette scale
  }

  getColorForValue(value: number, max: number): string {
    const ratio = max > 0 ? value / max : 0;
    const opacity = 0.2 + ratio * 0.8;
    return `rgba(13, 71, 161, ${opacity})`;
  }

  selectDistrict(d: DistrictData): void { this.districtSelected.emit(d); }
}
