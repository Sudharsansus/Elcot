// ============================================================
// AI REPORTS — natural-language analytics for officials.
// Ask a question in plain Tamil/English → KPI tiles + a chart + a narrative
// summary + AI insights, with CSV / print-to-PDF export. Read-only: the AI
// reports on data, it never approves or changes a record.
// ============================================================
import { Component, ChangeDetectionStrategy, inject, signal, DestroyRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ChartComponent } from '@avgc-xr/ui-kit';
import { AiReportingService, ReportResult } from '../ai-reporting.service';

@Component({
  selector: 'app-ai-reports',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, FormsModule, ChartComponent],
  templateUrl: './ai-reports.component.html',
  styleUrls: ['./ai-reports.component.scss'],
})
export class AiReportsComponent {
  private readonly reporting = inject(AiReportingService);
  private readonly translate = inject(TranslateService);
  private readonly destroyRef = inject(DestroyRef);

  readonly lang = signal<'en' | 'ta'>((this.translate.currentLang as 'en' | 'ta') || 'en');
  readonly draft = signal('');
  readonly busy = signal(false);
  readonly report = signal<ReportResult | null>(null);
  readonly suggestions = this.reporting.suggestions();
  private lastQuery = '';

  constructor() {
    this.translate.onLangChange.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((e) => {
      this.lang.set((e.lang as 'en' | 'ta') || 'en');
      if (this.lastQuery) this.report.set(this.reporting.generate(this.lastQuery, this.lang())); // re-localize
    });
  }

  t(en: string, ta: string): string { return this.lang() === 'ta' ? ta : en; }

  ask(q?: string): void {
    const query = (q ?? this.draft()).trim();
    if (!query || this.busy()) return;
    this.draft.set(query);
    this.lastQuery = query;
    this.busy.set(true);
    // brief "analysing" beat (mirrors the model round-trip once wired)
    setTimeout(() => {
      this.report.set(this.reporting.generate(query, this.lang()));
      this.busy.set(false);
    }, 450);
  }

  pick(q: string): void { this.ask(q); }

  exportCsv(): void {
    const r = this.report();
    if (!r?.table) return;
    const rows = [r.table.headers, ...r.table.rows];
    const csv = rows.map((row) => row.map((c) => `"${String(c).replace(/"/g, '""')}"`).join(',')).join('\n');
    const blob = new Blob(['﻿' + csv], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `${(r.title || 'report').replace(/\s+/g, '-').toLowerCase()}.csv`;
    a.click();
    URL.revokeObjectURL(url);
  }

  printReport(): void { window.print(); }
}
