import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-resource-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <section class="page">
      <h1>Resources</h1>
      <p>Policy documents, the Business Starter Toolkit and guidance materials.</p>
    </section>
  `
})
export class ResourceListComponent {}
