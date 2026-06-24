import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-about',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <section class="page">
      <h1>About the Tamil Nadu AVGC-XR Portal</h1>
      <p>A single digital interface for the Tamil Nadu AVGC-XR Policy ecosystem.</p>
    </section>
  `
})
export class AboutComponent {}
