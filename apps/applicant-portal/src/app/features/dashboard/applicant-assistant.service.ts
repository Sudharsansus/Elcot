// ============================================================
// APPLICANT AI ASSISTANT — Layer 5.
//
// Explains an application's status in plain language, tells the applicant what
// to do next, and surfaces reminders. Offline rule engine today; LLM-ready — the
// offline model produces the same plain-language answers from the live
// application + workflow data later. Read-only: it explains, never changes state.
//
// The snapshot below is clearly-labelled SAMPLE data until the application API is
// wired (demo:true).
// ============================================================
import { Injectable } from '@angular/core';

type Lang = 'en' | 'ta';

export interface AppSnapshot {
  ref: string;
  scheme: string; schemeTa: string;
  status: 'SUBMITTED' | 'UNDER_REVIEW' | 'APPROVED' | 'REJECTED' | 'ACTION_NEEDED';
  stage: string; stageTa: string;
  submitted: string;      // ISO date
  expectedDays: number;   // to next decision
  missing: string[]; missingTa: string[];
  deadline?: string;
  demo: boolean;
}

@Injectable({ providedIn: 'root' })
export class ApplicantAssistantService {
  /** Illustrative sample — replaced by GET /applications/mine when the API is wired. */
  readonly sample: AppSnapshot = {
    ref: 'APP-2026-004821',
    scheme: 'AVGC Production Subsidy', schemeTa: 'AVGC தயாரிப்பு மானியம்',
    status: 'UNDER_REVIEW',
    stage: 'District Officer — document verification', stageTa: 'மாவட்ட அலுவலர் — ஆவண சரிபார்ப்பு',
    submitted: '2026-06-28',
    expectedDays: 2,
    missing: ['Audited financial statement (FY 2024-25)'],
    missingTa: ['தணிக்கை நிதி அறிக்கை (2024-25)'],
    deadline: '2026-12-31',
    demo: true,
  };

  private t(lang: Lang, en: string, ta: string): string { return lang === 'ta' ? ta : en; }

  /** Plain-language status explainer (replaces a bare "UNDER_REVIEW"). */
  explain(a: AppSnapshot, lang: Lang): string {
    const stage = lang === 'ta' ? a.stageTa : a.stage;
    const scheme = lang === 'ta' ? a.scheme : a.scheme;
    switch (a.status) {
      case 'APPROVED':
        return this.t(lang, `Good news — your application (${a.ref}) for ${scheme} has been approved. Disbursal details will follow.`, `நற்செய்தி — ${a.ref} விண்ணப்பம் அங்கீகரிக்கப்பட்டது.`);
      case 'REJECTED':
        return this.t(lang, `Your application (${a.ref}) was not approved this time. You can view the reason and re-apply.`, `${a.ref} விண்ணப்பம் இம்முறை அங்கீகரிக்கப்படவில்லை.`);
      case 'ACTION_NEEDED':
        return this.t(lang, `Your application (${a.ref}) needs something from you before it can move forward — see the reminders below.`, `${a.ref} விண்ணப்பம் முன்னேற உங்களிடமிருந்து ஒன்று தேவை.`);
      case 'SUBMITTED':
        return this.t(lang, `Your application (${a.ref}) has been submitted and is in the queue for review.`, `${a.ref} விண்ணப்பம் சமர்ப்பிக்கப்பட்டு மதிப்பாய்வுக்கு காத்திருக்கிறது.`);
      default:
        return this.t(lang,
          `Your application (${a.ref}) for ${scheme} is currently with the ${stage}. At the current pace, a decision is expected in about ${a.expectedDays} day(s).`,
          `${a.ref} விண்ணப்பம் தற்போது ${stage} இடம் உள்ளது. சுமார் ${a.expectedDays} நாட்களில் முடிவு எதிர்பார்க்கப்படுகிறது.`);
    }
  }

  nextSteps(a: AppSnapshot, lang: Lang): string[] {
    const steps: string[] = [];
    const missing = lang === 'ta' ? a.missingTa : a.missing;
    if (missing.length) {
      steps.push(this.t(lang, `Upload the pending document: ${missing.join(', ')}.`, `நிலுவையில் உள்ள ஆவணத்தைப் பதிவேற்றவும்: ${missing.join(', ')}.`));
    }
    if (a.status === 'UNDER_REVIEW' && !missing.length) {
      steps.push(this.t(lang, 'No action needed right now — we’ll notify you when the officer updates it.', 'இப்போது நடவடிக்கை தேவையில்லை — புதுப்பிப்பு வரும்போது அறிவிப்போம்.'));
    }
    if (a.status === 'APPROVED') {
      steps.push(this.t(lang, 'Confirm your bank details for disbursal.', 'வழங்கலுக்கு உங்கள் வங்கி விவரங்களை உறுதிப்படுத்தவும்.'));
    }
    if (!steps.length) steps.push(this.t(lang, 'Keep an eye on notifications for the next update.', 'அடுத்த புதுப்பிப்புக்கு அறிவிப்புகளைக் கவனியுங்கள்.'));
    return steps;
  }

  reminders(a: AppSnapshot, lang: Lang): string[] {
    const out: string[] = [];
    const missing = lang === 'ta' ? a.missingTa : a.missing;
    for (const m of missing) out.push(this.t(lang, `Missing: ${m}`, `விடுபட்டது: ${m}`));
    if (a.deadline) out.push(this.t(lang, `Scheme deadline: ${a.deadline}`, `திட்ட காலக்கெடு: ${a.deadline}`));
    return out;
  }

  /** Free-text Q&A about the application (offline; model supersedes later). */
  ask(query: string, a: AppSnapshot, lang: Lang): string {
    const x = query.toLowerCase();
    if (/where|status|stage|with whom|which officer/.test(x)) return this.explain(a, lang);
    if (/how long|when|expect|time|days/.test(x)) return this.t(lang, `A decision is expected in about ${a.expectedDays} day(s), based on the current stage.`, `சுமார் ${a.expectedDays} நாட்களில் முடிவு எதிர்பார்க்கப்படுகிறது.`);
    if (/missing|document|pending|upload|need/.test(x)) {
      const missing = lang === 'ta' ? a.missingTa : a.missing;
      return missing.length
        ? this.t(lang, `Still needed: ${missing.join(', ')}. Upload it from "My Applications".`, `இன்னும் தேவை: ${missing.join(', ')}.`)
        : this.t(lang, 'Nothing is pending from your side right now.', 'உங்கள் பக்கத்தில் இப்போது எதுவும் நிலுவையில் இல்லை.');
    }
    if (/next|do|should i|what now/.test(x)) return this.nextSteps(a, lang).join(' ');
    return this.t(lang,
      'I can explain where your application is, what’s pending, and what to do next. Try "where is my application?" or "what’s pending?".',
      'உங்கள் விண்ணப்பம் எங்குள்ளது, என்ன நிலுவையில், அடுத்து என்ன செய்வது எனச் சொல்ல முடியும்.');
  }
}
