import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';

@Component({
  selector: 'app-scheme-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <section class="page">
      <h1>Scheme Details</h1>
      <p>Scheme reference: {{ id }}</p>
      <a routerLink="/schemes">&larr; Back to all schemes</a>
    </section>
  `
})
export class SchemeDetailComponent {
  private readonly route = inject(ActivatedRoute);
  readonly id = this.route.snapshot.paramMap.get('id');
}
