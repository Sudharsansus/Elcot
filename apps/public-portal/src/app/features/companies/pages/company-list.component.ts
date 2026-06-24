import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-company-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <section class="page">
      <h1>AVGC-XR Companies</h1>
      <p>Browse companies in the Tamil Nadu AVGC-XR ecosystem.</p>
    </section>
  `
})
export class CompanyListComponent {}
