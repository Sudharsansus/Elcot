import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-event-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <section class="page">
      <h1>Events & Announcements</h1>
      <p>Conferences, workshops, campaigns and sector-promotion activities.</p>
    </section>
  `
})
export class EventListComponent {}
