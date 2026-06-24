import { Injectable, inject, Component } from '@angular/core';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { MatDialog, MatDialogConfig, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private readonly snackBar = inject(MatSnackBar);
  private readonly dialog = inject(MatDialog);

  private readonly defaultConfig: MatSnackBarConfig = {
    duration: 4000,
    horizontalPosition: 'right',
    verticalPosition: 'top',
    panelClass: ['avgcxr-notification']
  };

  success(message: string, action: string = 'Close'): void {
    const config: MatSnackBarConfig = { ...this.defaultConfig, panelClass: ['avgcxr-notification', 'avgcxr-success'] };
    this.snackBar.open(message, action, config);
  }

  error(message: string, action: string = 'Close'): void {
    const config: MatSnackBarConfig = { ...this.defaultConfig, duration: 6000, panelClass: ['avgcxr-notification', 'avgcxr-error'] };
    this.snackBar.open(message, action, config);
  }

  warning(message: string, action: string = 'Close'): void {
    const config: MatSnackBarConfig = { ...this.defaultConfig, duration: 5000, panelClass: ['avgcxr-notification', 'avgcxr-warning'] };
    this.snackBar.open(message, action, config);
  }

  info(message: string, action: string = 'Close'): void {
    const config: MatSnackBarConfig = { ...this.defaultConfig, panelClass: ['avgcxr-notification', 'avgcxr-info'] };
    this.snackBar.open(message, action, config);
  }

  confirm(title: string, message: string): Promise<boolean> {
    return new Promise<boolean>((resolve) => {
      const config: MatDialogConfig = {
        width: '400px',
        disableClose: true,
        data: { title, message }
      };
      const dialogRef = this.dialog.open(ConfirmDialogComponent, config);
      dialogRef.afterClosed().subscribe((result: boolean) => resolve(!!result));
    });
  }
}

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule],
  template: `
    <h2 mat-dialog-title>{{ data.title }}</h2>
    <mat-dialog-content><p>{{ data.message }}</p></mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button mat-dialog-close>Cancel</button>
      <button mat-raised-button color="primary" [mat-dialog-close]="true">Confirm</button>
    </mat-dialog-actions>
  `
})
class ConfirmDialogComponent {
  data = inject(MAT_DIALOG_DATA) as { title: string; message: string };
}
