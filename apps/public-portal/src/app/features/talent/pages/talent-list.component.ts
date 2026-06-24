import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-talent-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <section class="page">
      <h1>Talent Connect</h1>
      <p>Discover skilled professionals across animation, VFX, gaming, comics and XR.</p>
    </section>
  `
})
export class TalentListComponent {}
