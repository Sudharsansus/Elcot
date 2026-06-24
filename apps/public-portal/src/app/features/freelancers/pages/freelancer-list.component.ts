import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-freelancer-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <section class="page">
      <h1>Freelancer Registry</h1>
      <p>Find registered AVGC-XR freelancers by skill and category.</p>
    </section>
  `
})
export class FreelancerListComponent {}
