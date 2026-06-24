import { Component, input, output, ViewEncapsulation } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface TableColumn { key: string; label: string; labelTamil?: string; sortable?: boolean; width?: string; align?: 'left' | 'center' | 'right'; }

@Component({
  selector: 'avgc-table',
  standalone: true, imports: [CommonModule],
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class TableComponent {
  protected readonly Math = Math;
  columns = input<TableColumn[]>([]);
  data = input<Record<string, any>[]>([]);
  loading = input<boolean>(false);
  emptyMessage = input<string>('No data available');
  sortable = input<boolean>(true);
  sortChanged = output<{ key: string; direction: 'ASC' | 'DESC' }>();
  rowClicked = output<Record<string, any>>();
  pageChanged = output<{ page: number; size: number }>();

  currentPage = input<number>(1);
  totalPages = input<number>(1);
  totalElements = input<number>(0);
  pageSize = input<number>(10);

  onSort(key: string): void {
    if (this.sortable()) { this.sortChanged.emit({ key, direction: 'ASC' }); }
  }
}
