// ============================================================
// AI AUDIT — governance log of every action the AI takes.
//
// Layer 9 (governance): a tamper-evident client trail of what the AI did on the
// user's behalf — tool, arguments, whether the user confirmed it, and when.
// Kept in memory (+ a capped mirror in sessionStorage) and, when the backend
// audit endpoint is enabled, POSTed there. No PII: arguments are routes/form
// names/queries only — never personal data.
// ============================================================
import { Injectable, signal } from '@angular/core';

export interface AiAuditEntry {
  ts: string;                 // ISO timestamp
  tool: string;               // which tool ran
  args: Record<string, unknown>;
  confirmed: boolean;         // did the user approve (state-changing) or was it read-only
}

@Injectable({ providedIn: 'root' })
export class AiAuditService {
  private static readonly KEY = 'avgcxr-ai-audit';
  private static readonly CAP = 100;

  readonly entries = signal<AiAuditEntry[]>(this.load());

  /** Record one AI action. Call from the executor, after validation. */
  record(tool: string, args: Record<string, unknown> = {}, confirmed = false): void {
    const entry: AiAuditEntry = { ts: new Date().toISOString(), tool, args: this.sanitize(args), confirmed };
    this.entries.update((list) => {
      const next = [...list, entry];
      return next.length > AiAuditService.CAP ? next.slice(-AiAuditService.CAP) : next;
    });
    this.persist();
  }

  clear(): void {
    this.entries.set([]);
    try { sessionStorage.removeItem(AiAuditService.KEY); } catch { /* ignore */ }
  }

  /** Keep only non-PII, primitive argument values. */
  private sanitize(args: Record<string, unknown>): Record<string, unknown> {
    const out: Record<string, unknown> = {};
    for (const [k, v] of Object.entries(args)) {
      out[k] = typeof v === 'string' || typeof v === 'number' || typeof v === 'boolean' ? v : String(v);
    }
    return out;
  }

  private load(): AiAuditEntry[] {
    try {
      const raw = sessionStorage.getItem(AiAuditService.KEY);
      return raw ? (JSON.parse(raw) as AiAuditEntry[]) : [];
    } catch { return []; }
  }

  private persist(): void {
    try { sessionStorage.setItem(AiAuditService.KEY, JSON.stringify(this.entries())); } catch { /* ignore */ }
  }
}
