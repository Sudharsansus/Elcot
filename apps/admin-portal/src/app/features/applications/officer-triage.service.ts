// ============================================================
// OFFICER AI TRIAGE — Layer 7 (decision SUPPORT, never decision).
//
// For a reviewing officer: a plain-language summary of an application, risk and
// duplicate-detection flags, and a recommendation WITH its reason and confidence.
// The officer decides — the AI never approves, rejects, sanctions or pays.
// Offline rule engine today; LLM-ready (same TriageResult from live data later).
//
// The application below is clearly-labelled SAMPLE data (demo:true).
// ============================================================
import { Injectable } from '@angular/core';

type Lang = 'en' | 'ta';

export interface TriageApp {
  ref: string;
  applicant: string;
  scheme: string; schemeTa: string;
  district: string;
  amount: number;
  submitted: string;
  duplicate: { pan?: number; gst?: number; phone?: number }; // count of other apps sharing the value
  sameIpCount?: number;
  missingDocs: string[]; missingDocsTa: string[];
  demo: boolean;
}

export interface TriageRisk { label: string; level: 'low' | 'medium' | 'high'; }
export interface TriageResult {
  summary: string;
  risks: TriageRisk[];
  duplicates: string[];
  recommendation: string; // a SUGGESTION with a reason — never "approve"
  confidence: number;     // 0..1
  demo: boolean;
}

@Injectable({ providedIn: 'root' })
export class OfficerTriageService {
  /** Illustrative sample — replaced by the application under review when the API is wired. */
  readonly sample: TriageApp = {
    ref: 'APP-2026-004821',
    applicant: 'Nova Frames Studio Pvt Ltd',
    scheme: 'AVGC Production Subsidy', schemeTa: 'AVGC தயாரிப்பு மானியம்',
    district: 'Chennai',
    amount: 4200000,
    submitted: '2026-06-28',
    duplicate: { pan: 2, gst: 0, phone: 1 },
    sameIpCount: 3,
    missingDocs: ['Audited financial statement (FY 2024-25)'],
    missingDocsTa: ['தணிக்கை நிதி அறிக்கை (2024-25)'],
    demo: true,
  };

  private t(lang: Lang, en: string, ta: string): string { return lang === 'ta' ? ta : en; }

  triage(a: TriageApp, lang: Lang): TriageResult {
    const scheme = lang === 'ta' ? a.schemeTa : a.scheme;
    const amt = `₹${(a.amount / 100000).toFixed(1)}L`;
    const summary = this.t(lang,
      `${a.applicant} (${a.district}) has applied to the ${scheme} for ${amt}, submitted ${a.submitted}. Documentation is ${a.missingDocs.length ? 'incomplete' : 'complete'}.`,
      `${a.applicant} (${a.district}) ${scheme} — ${amt}, ${a.submitted} அன்று சமர்ப்பிக்கப்பட்டது. ஆவணங்கள் ${a.missingDocs.length ? 'முழுமையடையவில்லை' : 'முழுமை'}.`);

    const risks: TriageRisk[] = [];
    if (a.missingDocs.length) {
      risks.push({ label: this.t(lang, `Incomplete documentation (${a.missingDocs.length} missing)`, `முழுமையற்ற ஆவணங்கள் (${a.missingDocs.length})`), level: 'medium' });
    }
    if ((a.sameIpCount ?? 0) > 1) {
      risks.push({ label: this.t(lang, `${a.sameIpCount} submissions from the same IP address`, `${a.sameIpCount} விண்ணப்பங்கள் ஒரே IP-யிலிருந்து`), level: 'medium' });
    }
    if ((a.duplicate.pan ?? 0) > 0) {
      risks.push({ label: this.t(lang, 'PAN shared with other applications', 'PAN மற்ற விண்ணப்பங்களுடன் பகிரப்பட்டது'), level: 'high' });
    }
    if (!risks.length) risks.push({ label: this.t(lang, 'No material risk indicators', 'குறிப்பிடத்தக்க அபாய அறிகுறிகள் இல்லை'), level: 'low' });

    const duplicates: string[] = [];
    if (a.duplicate.pan) duplicates.push(this.t(lang, `Same PAN as ${a.duplicate.pan} other application(s)`, `${a.duplicate.pan} விண்ணப்பங்களில் அதே PAN`));
    if (a.duplicate.gst) duplicates.push(this.t(lang, `Same GST as ${a.duplicate.gst} other application(s)`, `${a.duplicate.gst} விண்ணப்பங்களில் அதே GST`));
    if (a.duplicate.phone) duplicates.push(this.t(lang, `Shared phone with ${a.duplicate.phone} applicant(s)`, `${a.duplicate.phone} விண்ணப்பதாரருடன் பகிர்ந்த தொலைபேசி`));

    const highest = risks.some((r) => r.level === 'high') ? 'high' : risks.some((r) => r.level === 'medium') ? 'medium' : 'low';
    const recommendation = highest === 'low'
      ? this.t(lang, 'Suggested: eligible for standard processing. Verify documents, then decide.', 'பரிந்துரை: வழக்கமான செயலாக்கத்திற்கு தகுதி. ஆவணங்களைச் சரிபார்த்து முடிவெடுக்கவும்.')
      : this.t(lang,
          `Suggested: hold for verification — request the missing document and check the duplicate-PAN link before deciding. This is guidance only; the decision is yours.`,
          `பரிந்துரை: சரிபார்ப்புக்காக நிறுத்தவும் — விடுபட்ட ஆவணத்தைக் கோரி, நகல் PAN தொடர்பைச் சரிபார்க்கவும். இது வழிகாட்டுதல் மட்டுமே; முடிவு உங்களுடையது.`);

    const confidence = highest === 'high' ? 0.72 : highest === 'medium' ? 0.81 : 0.9;

    return { summary, risks, duplicates, recommendation, confidence, demo: a.demo };
  }
}
