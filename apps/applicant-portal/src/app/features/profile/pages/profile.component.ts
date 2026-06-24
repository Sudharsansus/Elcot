import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonComponent, InputComponent, SelectComponent } from '@avgc-xr/ui-kit';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-profile', standalone: true, imports: [FormsModule, ButtonComponent, InputComponent, SelectComponent, CommonModule],
  templateUrl: './profile.component.html'
})
export class ProfileComponent {
  readonly districts: { value: string; label: string }[] = [{ value: 'CHE', label: 'Chennai' }, { value: 'CBE', label: 'Coimbatore' }, { value: 'MDU', label: 'Madurai' }];
  profileForm = { fullName: '', fullNameTamil: '', email: '', mobile: '', district: '' };
  saving = false;
  async saveProfile() { this.saving = true; /* API call */ this.saving = false; }
}
