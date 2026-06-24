import { Component, input, output, ViewEncapsulation } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'avgc-drawer',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './drawer.component.html',
  styleUrls: ['./drawer.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class DrawerComponent {
  isOpen = input<boolean>(false);
  position = input<'left' | 'right'>('right');
  title = input<string>('');
  width = input<string>('400px');
  closed = output<void>();

  close(): void { this.closed.emit(); }

  onBackdropClick(event: MouseEvent): void { if ((event.target as HTMLElement).classList.contains('avgc-drawer__backdrop')) this.close(); }

  onKeyDown(event: KeyboardEvent): void { if (event.key === 'Escape') this.close(); }
}
