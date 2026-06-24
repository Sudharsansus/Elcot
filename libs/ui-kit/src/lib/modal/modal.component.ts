import { Component, input, output, ViewEncapsulation } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'avgc-modal',
  standalone: true, imports: [CommonModule],
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class ModalComponent {
  isOpen = input<boolean>(false);
  title = input<string>('');
  size = input<'sm' | 'md' | 'lg' | 'full'>('md');
  closeOnBackdrop = input<boolean>(true);
  closed = output<void>();
  confirmed = output<void>();

  close(): void { this.closed.emit(); }

  confirm(): void { this.confirmed.emit(); }

  onBackdropClick(): void { if (this.closeOnBackdrop()) this.close(); }

  onKeyDown(e: KeyboardEvent): void { if (e.key === 'Escape') this.close(); }
}
