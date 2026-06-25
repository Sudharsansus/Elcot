import { Component, ChangeDetectionStrategy, inject, signal, computed, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { I18nService } from '../../../core/services/i18n.service';
import { StrapiContentService, EventItem } from '../../../core/services/strapi-content.service';
import { PageHeaderComponent, Crumb } from '../../../shared/page-header/page-header.component';
import { StateMessageComponent } from '../../../shared/state-message.component';

@Component({
  selector: 'app-event-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, RouterModule, MatIconModule, PageHeaderComponent, StateMessageComponent],
  template: `
    <app-page-header
      [icon]="'event'"
      [title]="ta() ? 'நிகழ்வுகள் & அறிவிப்புகள்' : 'Events & Announcements'"
      [subtitle]="ta() ? 'மாநாடுகள், பட்டறைகள் மற்றும் துறை-மேம்பாட்டு நிகழ்வுகள்' : 'Conferences, workshops and sector-promotion activities'"
      [homeLabel]="ta() ? 'முகப்பு' : 'Home'"
      [breadcrumb]="crumbs()">
    </app-page-header>

    <section class="ev">
      <div class="container">
        @if (loading()) {
          <app-state-message variant="loading" [title]="ta() ? 'நிகழ்வுகளை ஏற்றுகிறது…' : 'Loading events…'"></app-state-message>
        } @else if (error()) {
          <app-state-message
            variant="service-unavailable"
            [title]="ta() ? 'நிகழ்வுகளை ஏற்ற முடியவில்லை' : 'Events are temporarily unavailable'"
            [description]="ta() ? 'உள்ளடக்க சேவையை இப்போது அணுக முடியவில்லை. சிறிது நேரம் கழித்து முயற்சிக்கவும்.' : 'The content service can’t be reached right now. Please try again shortly.'">
          </app-state-message>
        } @else if (events().length) {
          <div class="ev-grid">
            @for (e of events(); track e.documentId) {
              <article class="ev-card">
                <div class="ev-date">
                  <span class="ev-day">{{ dayOf(e.eventDate) }}</span>
                  <span class="ev-mon">{{ monthOf(e.eventDate) }}</span>
                </div>
                <div class="ev-body">
                  <h3 class="ev-title">{{ e.title }}</h3>
                  @if (e.location) {
                    <p class="ev-loc"><mat-icon aria-hidden="true">place</mat-icon>{{ e.location }}</p>
                  }
                  @if (e.description) { <p class="ev-desc">{{ e.description }}</p> }
                  @if (e.registrationUrl) {
                    <a [href]="e.registrationUrl" target="_blank" rel="noopener" class="ev-cta">
                      {{ ta() ? 'பதிவு செய்க' : 'Register' }}<mat-icon aria-hidden="true">open_in_new</mat-icon>
                    </a>
                  }
                </div>
              </article>
            }
          </div>
        } @else {
          <app-state-message
            variant="empty"
            [title]="ta() ? 'வரவிருக்கும் நிகழ்வுகள் எதுவும் இல்லை' : 'No upcoming events yet'"
            [description]="ta() ? 'புதிய நிகழ்வுகள் வெளியிடப்படும்போது இங்கே தோன்றும். விரைவில் மீண்டும் பார்க்கவும்.' : 'New events appear here as they are published. Please check back soon.'">
          </app-state-message>
        }
      </div>
    </section>
  `,
  styles: [`
    :host { display: block; }
    .ev { padding: clamp(28px, 5vw, 56px) 0 clamp(48px, 7vw, 80px); }
    .ev-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 20px; }
    .ev-card {
      display: flex; gap: 18px; padding: 22px;
      background: var(--color-surface); border: 1px solid var(--color-border);
      border-radius: var(--radius-lg);
      transition: transform var(--duration-normal) var(--easing-standard), box-shadow var(--duration-normal) ease;
    }
    .ev-card:hover { transform: translateY(-4px); box-shadow: var(--shadow-md); }
    .ev-date {
      flex-shrink: 0; width: 64px; height: 64px;
      display: flex; flex-direction: column; align-items: center; justify-content: center;
      background: var(--color-primary); color: #fff; border-radius: var(--radius-md);
    }
    .ev-day { font-family: var(--font-serif); font-size: var(--text-2xl); font-weight: 800; line-height: 1; }
    .ev-mon { font-size: var(--text-xs); text-transform: uppercase; letter-spacing: 0.06em; }
    .ev-body { min-width: 0; }
    .ev-title { font-size: var(--text-lg); font-weight: 700; color: var(--color-text-primary); margin: 0 0 6px; line-height: 1.3; }
    .ev-loc { display: flex; align-items: center; gap: 5px; font-size: var(--text-sm); color: var(--color-text-secondary); margin: 0 0 8px; }
    .ev-loc mat-icon { font-size: 16px; width: 16px; height: 16px; color: var(--color-primary); }
    .ev-desc { font-size: var(--text-sm); color: var(--color-text-secondary); line-height: 1.6; margin: 0 0 12px; }
    .ev-cta { display: inline-flex; align-items: center; gap: 6px; font-size: var(--text-sm); font-weight: 700; color: var(--color-primary); text-decoration: none; }
    .ev-cta mat-icon { font-size: 16px; width: 16px; height: 16px; }
    .ev-cta:hover { text-decoration: underline; }
  `],
})
export class EventListComponent implements OnInit {
  private readonly i18n = inject(I18nService);
  private readonly strapi = inject(StrapiContentService);

  readonly ta = computed(() => this.i18n.currentLanguage() === 'ta');
  readonly crumbs = computed<Crumb[]>(() => [{ label: this.ta() ? 'நிகழ்வுகள்' : 'Events' }]);

  readonly events = signal<EventItem[]>([]);
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);

  async ngOnInit(): Promise<void> {
    this.loading.set(true);
    try {
      const items = await this.strapi.fetchEvents({ limit: 24 });
      this.events.set(items);
      this.error.set(this.strapi.error());
    } finally {
      this.loading.set(false);
    }
  }

  private d(dateStr: string): Date { return new Date(dateStr); }
  dayOf(dateStr: string): string { return this.d(dateStr).toLocaleDateString('en-IN', { day: '2-digit' }); }
  monthOf(dateStr: string): string { return this.d(dateStr).toLocaleDateString(this.ta() ? 'ta-IN' : 'en-IN', { month: 'short' }); }
}
