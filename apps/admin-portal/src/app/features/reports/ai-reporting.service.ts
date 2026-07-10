// ============================================================
// AI REPORTING — natural-language analytics for officials.
//
// "How many animation studios registered this quarter?" → a structured report:
// KPI tiles + a chart + a narrative summary + AI insights, all bilingual.
//
// LLM-ready by design. Today an offline rule engine maps the question to a
// report spec over aggregated figures. When the fine-tuned offline model + the
// reporting API are wired, the model produces the SAME ReportResult shape from
// a live aggregation query — the rendering layer is unchanged. No API keys; the
// model and data stay on the government server. Reports are READ-ONLY: AI never
// approves, sanctions or changes a record.
//
// NOTE: the figures below are clearly-labelled ILLUSTRATIVE SAMPLE data so the
// capability is demonstrable before the reporting API is connected (demo:true).
// ============================================================
import { Injectable } from '@angular/core';
import type { EChartsOption } from 'echarts';

/** Subset of avgc-chart's ChartType we render (assignable to its input). */
export type ReportChartType = 'bar' | 'line' | 'pie';
export type MetricTone = 'good' | 'warn' | 'bad' | 'neutral';
export interface ReportMetric { label: string; value: string; delta?: string; trend?: 'up' | 'down' | 'flat'; tone?: MetricTone; }
export interface ReportResult {
  title: string;
  summary: string;              // narrative, generated from the figures
  metrics: ReportMetric[];      // KPI tiles
  chart: EChartsOption | null;  // single-hue (magnitude) chart
  chartType: ReportChartType;
  table: { headers: string[]; rows: (string | number)[][] } | null;
  insights: string[];           // AI insight bullets (trend / concentration / anomaly)
  demo: boolean;                // true → illustrative sample figures
}
export interface ReportQuery { en: string; ta: string; q: string; }

type Lang = 'en' | 'ta';

// ---- validated marks (dataviz skill): single violet hue for magnitude ----
const SERIES = '#7c3aed';
const AXIS_LINE = '#cbd5e1';
const AXIS_TEXT = '#64748b';
const LABEL_TEXT = '#475569';
const SPLIT_LINE = '#eef2f7';

@Injectable({ providedIn: 'root' })
export class AiReportingService {
  // ---- illustrative sample aggregates (replaced by the reporting API) ----
  private readonly sectors = [
    { en: 'Animation', ta: 'அனிமேஷன்', regs: 412 },
    { en: 'VFX', ta: 'விஎஃப்எக்ஸ்', regs: 268 },
    { en: 'Gaming', ta: 'கேமிங்', regs: 331 },
    { en: 'Comics', ta: 'காமிக்ஸ்', regs: 96 },
    { en: 'XR (AR/VR)', ta: 'XR (AR/VR)', regs: 154 },
  ];
  private readonly districts = [
    { en: 'Chennai', ta: 'சென்னை', regs: 486 },
    { en: 'Coimbatore', ta: 'கோயம்புத்தூர்', regs: 214 },
    { en: 'Madurai', ta: 'மதுரை', regs: 132 },
    { en: 'Tiruchirappalli', ta: 'திருச்சி', regs: 98 },
    { en: 'Salem', ta: 'சேலம்', regs: 71 },
  ];
  private readonly months = [
    { en: 'Nov', ta: 'நவ', regs: 118 },
    { en: 'Dec', ta: 'டிச', regs: 142 },
    { en: 'Jan', ta: 'ஜன', regs: 161 },
    { en: 'Feb', ta: 'பிப்', regs: 175 },
    { en: 'Mar', ta: 'மார்', regs: 198 },
    { en: 'Apr', ta: 'ஏப்', regs: 224 },
    { en: 'May', ta: 'மே', regs: 251 },
    { en: 'Jun', ta: 'ஜூன்', regs: 289 },
  ];
  private readonly pipeline = [
    { en: 'Applied', ta: 'விண்ணப்பித்தது', n: 1261 },
    { en: 'Under Review', ta: 'மதிப்பாய்வில்', n: 486 },
    { en: 'Approved', ta: 'அங்கீகரிக்கப்பட்டது', n: 642 },
    { en: 'Rejected', ta: 'நிராகரிக்கப்பட்டது', n: 133 },
  ];
  private readonly schemes = [
    { en: 'Production Subsidy', ta: 'உற்பத்தி மானியம்', n: 384 },
    { en: 'Skill Training', ta: 'திறன் பயிற்சி', n: 512 },
    { en: 'XR Infrastructure', ta: 'XR உள்கட்டமைப்பு', n: 143 },
    { en: 'Market Access', ta: 'சந்தை அணுகல்', n: 176 },
    { en: 'Freelancer Support', ta: 'ஃப்ரீலான்சர் ஆதரவு', n: 268 },
    { en: 'Scholarships', ta: 'உதவித்தொகை', n: 421 },
  ];
  private readonly disbursedCr = 18.7; // ₹ crore

  private t(lang: Lang, en: string, ta: string): string { return lang === 'ta' ? ta : en; }

  /** Suggested questions to seed the reporting bar. */
  suggestions(): ReportQuery[] {
    return [
      { en: 'Registrations by sector', ta: 'துறை வாரியாக பதிவுகள்', q: 'registrations by sector' },
      { en: 'Monthly registration trend', ta: 'மாதாந்திர பதிவு போக்கு', q: 'monthly registration trend' },
      { en: 'District-wise distribution', ta: 'மாவட்ட வாரியான பரவல்', q: 'district wise distribution' },
      { en: 'Application pipeline status', ta: 'விண்ணப்ப நிலை', q: 'application pipeline status' },
      { en: 'Scheme uptake', ta: 'திட்ட பயன்பாடு', q: 'scheme uptake' },
      { en: 'Overview', ta: 'மேலோட்டம்', q: 'overview' },
    ];
  }

  /**
   * Turn a natural-language question into a structured report.
   * (Offline rule engine; the offline model supersedes the matching later.)
   */
  generate(query: string, lang: Lang = 'en'): ReportResult {
    const x = ` ${query.toLowerCase()} `;
    const has = (...k: string[]) => k.some((s) => x.includes(s));

    if (has('sector', 'animation', 'vfx', 'gaming', 'comics', ' xr ', 'category')) return this.bySector(lang);
    if (has('month', 'trend', 'over time', 'growth', 'timeline', 'quarter', 'this year')) return this.monthlyTrend(lang);
    if (has('district', 'region', 'chennai', 'coimbatore', 'madurai', 'geograph', 'where')) return this.byDistrict(lang);
    if (has('pipeline', 'status', 'pending', 'approved', 'rejected', 'review', 'stage')) return this.pipelineReport(lang);
    if (has('scheme', 'uptake', 'grant', 'subsid', 'program', 'programme')) return this.schemeUptake(lang);
    return this.overview(lang);
  }

  // ---- report builders ----
  private bySector(lang: Lang): ReportResult {
    const rows = [...this.sectors].sort((a, b) => b.regs - a.regs);
    const total = rows.reduce((s, r) => s + r.regs, 0);
    const top = rows[0];
    const share = Math.round((top.regs / total) * 100);
    return {
      title: this.t(lang, 'Registrations by sector', 'துறை வாரியாக பதிவுகள்'),
      summary: this.t(lang,
        `Across ${total.toLocaleString()} registrations, ${this.t(lang, top.en, top.ta)} leads with ${top.regs.toLocaleString()} (${share}%), followed by Gaming and VFX. Comics remains the smallest segment.`,
        `${total.toLocaleString()} பதிவுகளில், ${this.t(lang, top.en, top.ta)} ${top.regs.toLocaleString()} (${share}%) உடன் முன்னணியில் உள்ளது.`),
      metrics: [
        { label: this.t(lang, 'Total registrations', 'மொத்த பதிவுகள்'), value: total.toLocaleString(), tone: 'neutral' },
        { label: this.t(lang, 'Leading sector', 'முன்னணித் துறை'), value: this.t(lang, top.en, top.ta), delta: `${share}%`, tone: 'good' },
        { label: this.t(lang, 'Active sectors', 'செயலில் உள்ள துறைகள்'), value: String(rows.length), tone: 'neutral' },
      ],
      chart: this.barOption(rows.map((r) => this.t(lang, r.en, r.ta)), rows.map((r) => r.regs), this.t(lang, 'Registrations', 'பதிவுகள்')),
      chartType: 'bar',
      table: { headers: [this.t(lang, 'Sector', 'துறை'), this.t(lang, 'Registrations', 'பதிவுகள்')], rows: rows.map((r) => [this.t(lang, r.en, r.ta), r.regs]) },
      insights: [
        this.t(lang, `${this.t(lang, top.en, top.ta)} accounts for ${share}% of all registrations.`, `${this.t(lang, top.en, top.ta)} அனைத்து பதிவுகளிலும் ${share}%.`),
        this.t(lang, `The top 3 sectors make up ${Math.round(((rows[0].regs + rows[1].regs + rows[2].regs) / total) * 100)}% of the total.`, `முதல் 3 துறைகள் மொத்தத்தில் ${Math.round(((rows[0].regs + rows[1].regs + rows[2].regs) / total) * 100)}%.`),
      ],
      demo: true,
    };
  }

  private monthlyTrend(lang: Lang): ReportResult {
    const first = this.months[0].regs;
    const last = this.months[this.months.length - 1].regs;
    const growth = Math.round(((last - first) / first) * 100);
    const total = this.months.reduce((s, m) => s + m.regs, 0);
    return {
      title: this.t(lang, 'Monthly registration trend', 'மாதாந்திர பதிவு போக்கு'),
      summary: this.t(lang,
        `Registrations grew ${growth}% over the last ${this.months.length} months — from ${first} to ${last} per month — a steady upward trajectory with no monthly decline.`,
        `கடந்த ${this.months.length} மாதங்களில் பதிவுகள் ${growth}% வளர்ந்தன — மாதம் ${first}-லிருந்து ${last} வரை.`),
      metrics: [
        { label: this.t(lang, 'Growth (8 mo)', 'வளர்ச்சி (8 மா)'), value: `+${growth}%`, trend: 'up', tone: 'good' },
        { label: this.t(lang, 'Latest month', 'சமீபத்திய மாதம்'), value: last.toLocaleString(), tone: 'neutral' },
        { label: this.t(lang, 'Cumulative', 'ஒட்டுமொத்தம்'), value: total.toLocaleString(), tone: 'neutral' },
      ],
      chart: this.lineOption(this.months.map((m) => this.t(lang, m.en, m.ta)), this.months.map((m) => m.regs), this.t(lang, 'Registrations', 'பதிவுகள்')),
      chartType: 'line',
      table: { headers: [this.t(lang, 'Month', 'மாதம்'), this.t(lang, 'Registrations', 'பதிவுகள்')], rows: this.months.map((m) => [this.t(lang, m.en, m.ta), m.regs]) },
      insights: [
        this.t(lang, `Month-on-month growth averaged ${Math.round(growth / (this.months.length - 1))}%.`, `மாதாந்திர சராசரி வளர்ச்சி ${Math.round(growth / (this.months.length - 1))}%.`),
        this.t(lang, `Forecast: at the current rate next month is likely ~${Math.round(last * (1 + growth / 100 / (this.months.length - 1)))}.`, `முன்னறிவிப்பு: அடுத்த மாதம் ~${Math.round(last * (1 + growth / 100 / (this.months.length - 1)))}.`),
      ],
      demo: true,
    };
  }

  private byDistrict(lang: Lang): ReportResult {
    const rows = [...this.districts].sort((a, b) => b.regs - a.regs);
    const total = rows.reduce((s, r) => s + r.regs, 0);
    const top = rows[0];
    const share = Math.round((top.regs / total) * 100);
    return {
      title: this.t(lang, 'District-wise registrations', 'மாவட்ட வாரியான பதிவுகள்'),
      summary: this.t(lang,
        `${this.t(lang, top.en, top.ta)} leads with ${share}% of registrations, reflecting the studio cluster there. Tier-2 districts (Coimbatore, Madurai) are growing but under-represented.`,
        `${this.t(lang, top.en, top.ta)} ${share}% பதிவுகளுடன் முன்னணியில். இரண்டாம் நிலை மாவட்டங்கள் வளர்ந்து வருகின்றன.`),
      metrics: [
        { label: this.t(lang, 'Districts active', 'செயலில் மாவட்டங்கள்'), value: String(rows.length), tone: 'neutral' },
        { label: this.t(lang, 'Top district', 'முதன்மை மாவட்டம்'), value: this.t(lang, top.en, top.ta), delta: `${share}%`, tone: 'good' },
        { label: this.t(lang, 'Concentration', 'குவிப்பு'), value: share >= 40 ? this.t(lang, 'High', 'அதிகம்') : this.t(lang, 'Moderate', 'நடுத்தரம்'), tone: share >= 40 ? 'warn' : 'neutral' },
      ],
      chart: this.barOption(rows.map((r) => this.t(lang, r.en, r.ta)), rows.map((r) => r.regs), this.t(lang, 'Registrations', 'பதிவுகள்')),
      chartType: 'bar',
      table: { headers: [this.t(lang, 'District', 'மாவட்டம்'), this.t(lang, 'Registrations', 'பதிவுகள்')], rows: rows.map((r) => [this.t(lang, r.en, r.ta), r.regs]) },
      insights: [
        this.t(lang, `${this.t(lang, top.en, top.ta)} alone contributes ${share}% — consider outreach to widen the base.`, `${this.t(lang, top.en, top.ta)} மட்டும் ${share}% — அடித்தளத்தை விரிவுபடுத்த பரப்புரை.`),
      ],
      demo: true,
    };
  }

  private pipelineReport(lang: Lang): ReportResult {
    const applied = this.pipeline[0].n;
    const approved = this.pipeline.find((p) => p.en === 'Approved')!.n;
    const rejected = this.pipeline.find((p) => p.en === 'Rejected')!.n;
    const decided = approved + rejected;
    const approvalRate = Math.round((approved / decided) * 100);
    return {
      title: this.t(lang, 'Application pipeline', 'விண்ணப்ப பணிப்பாய்வு'),
      summary: this.t(lang,
        `Of ${applied.toLocaleString()} applications, ${this.pipeline[1].n} are under review. The approval rate among decided cases is ${approvalRate}%.`,
        `${applied.toLocaleString()} விண்ணப்பங்களில், ${this.pipeline[1].n} மதிப்பாய்வில். அங்கீகார விகிதம் ${approvalRate}%.`),
      metrics: [
        { label: this.t(lang, 'Under review', 'மதிப்பாய்வில்'), value: this.pipeline[1].n.toLocaleString(), tone: 'warn' },
        { label: this.t(lang, 'Approval rate', 'அங்கீகார விகிதம்'), value: `${approvalRate}%`, tone: 'good' },
        { label: this.t(lang, 'Rejected', 'நிராகரிக்கப்பட்டது'), value: rejected.toLocaleString(), tone: 'bad' },
      ],
      chart: this.barOption(this.pipeline.map((p) => this.t(lang, p.en, p.ta)), this.pipeline.map((p) => p.n), this.t(lang, 'Applications', 'விண்ணப்பங்கள்')),
      chartType: 'bar',
      table: { headers: [this.t(lang, 'Stage', 'நிலை'), this.t(lang, 'Count', 'எண்ணிக்கை')], rows: this.pipeline.map((p) => [this.t(lang, p.en, p.ta), p.n]) },
      insights: [
        this.t(lang, `${this.pipeline[1].n} applications await review — the main bottleneck.`, `${this.pipeline[1].n} விண்ணப்பங்கள் மதிப்பாய்வுக்கு காத்திருக்கின்றன — முக்கிய தடை.`),
        this.t(lang, 'AI does not approve or reject — these figures support officer decisions only.', 'AI அங்கீகரிக்காது/நிராகரிக்காது — இவை அதிகாரி முடிவுகளுக்கு துணை மட்டுமே.'),
      ],
      demo: true,
    };
  }

  private schemeUptake(lang: Lang): ReportResult {
    const rows = [...this.schemes].sort((a, b) => b.n - a.n);
    const total = rows.reduce((s, r) => s + r.n, 0);
    const top = rows[0];
    return {
      title: this.t(lang, 'Scheme uptake', 'திட்ட பயன்பாடு'),
      summary: this.t(lang,
        `${this.t(lang, top.en, top.ta)} is the most-applied scheme (${top.n}), with Scholarships and Production Subsidy close behind. XR Infrastructure has the lowest uptake.`,
        `${this.t(lang, top.en, top.ta)} அதிகம் விண்ணப்பிக்கப்பட்ட திட்டம் (${top.n}).`),
      metrics: [
        { label: this.t(lang, 'Total applications', 'மொத்த விண்ணப்பங்கள்'), value: total.toLocaleString(), tone: 'neutral' },
        { label: this.t(lang, 'Most popular', 'மிகவும் பிரபலம்'), value: this.t(lang, top.en, top.ta), tone: 'good' },
        { label: this.t(lang, 'Funds disbursed', 'வழங்கப்பட்ட நிதி'), value: `₹${this.disbursedCr} Cr`, tone: 'neutral' },
      ],
      chart: this.barOption(rows.map((r) => this.t(lang, r.en, r.ta)), rows.map((r) => r.n), this.t(lang, 'Applications', 'விண்ணப்பங்கள்')),
      chartType: 'bar',
      table: { headers: [this.t(lang, 'Scheme', 'திட்டம்'), this.t(lang, 'Applications', 'விண்ணப்பங்கள்')], rows: rows.map((r) => [this.t(lang, r.en, r.ta), r.n]) },
      insights: [
        this.t(lang, `Training-oriented schemes dominate demand — signal to expand capacity.`, `பயிற்சி சார்ந்த திட்டங்கள் அதிக தேவை — திறனை விரிவாக்க சமிக்ஞை.`),
      ],
      demo: true,
    };
  }

  private overview(lang: Lang): ReportResult {
    const totalReg = this.sectors.reduce((s, r) => s + r.regs, 0);
    const applied = this.pipeline[0].n;
    const growth = Math.round(((this.months[this.months.length - 1].regs - this.months[0].regs) / this.months[0].regs) * 100);
    return {
      title: this.t(lang, 'Portal overview', 'போர்ட்டல் மேலோட்டம்'),
      summary: this.t(lang,
        `${totalReg.toLocaleString()} registrations and ${applied.toLocaleString()} applications to date, with registrations up ${growth}% over 8 months. Ask for a sector, district, scheme or pipeline breakdown for detail.`,
        `இதுவரை ${totalReg.toLocaleString()} பதிவுகள், ${applied.toLocaleString()} விண்ணப்பங்கள்; பதிவுகள் 8 மாதங்களில் ${growth}% அதிகரித்தன.`),
      metrics: [
        { label: this.t(lang, 'Registrations', 'பதிவுகள்'), value: totalReg.toLocaleString(), trend: 'up', delta: `+${growth}%`, tone: 'good' },
        { label: this.t(lang, 'Applications', 'விண்ணப்பங்கள்'), value: applied.toLocaleString(), tone: 'neutral' },
        { label: this.t(lang, 'Under review', 'மதிப்பாய்வில்'), value: this.pipeline[1].n.toLocaleString(), tone: 'warn' },
        { label: this.t(lang, 'Disbursed', 'வழங்கப்பட்டது'), value: `₹${this.disbursedCr} Cr`, tone: 'neutral' },
      ],
      chart: this.lineOption(this.months.map((m) => this.t(lang, m.en, m.ta)), this.months.map((m) => m.regs), this.t(lang, 'Registrations', 'பதிவுகள்')),
      chartType: 'line',
      table: null,
      insights: [
        this.t(lang, `Growth is accelerating — the last two months added the most registrations.`, `வளர்ச்சி துரிதப்படுகிறது — கடந்த இரு மாதங்களில் அதிக பதிவுகள்.`),
        this.t(lang, `Try: "registrations by sector" or "application pipeline status".`, `முயற்சி: "துறை வாரியாக பதிவுகள்" அல்லது "விண்ணப்ப நிலை".`),
      ],
      demo: true,
    };
  }

  // ---- chart options: single violet hue (magnitude), recessive axes ----
  private barOption(cats: string[], vals: number[], name: string): EChartsOption {
    return {
      tooltip: { trigger: 'axis' },
      grid: { left: '3%', right: '4%', bottom: '3%', top: 28, containLabel: true },
      xAxis: { type: 'category', data: cats, axisLine: { lineStyle: { color: AXIS_LINE } }, axisLabel: { color: AXIS_TEXT, interval: 0, hideOverlap: true } },
      yAxis: { type: 'value', splitLine: { lineStyle: { color: SPLIT_LINE } }, axisLabel: { color: AXIS_TEXT } },
      series: [{
        name, type: 'bar', data: vals, barMaxWidth: 46,
        itemStyle: { color: SERIES, borderRadius: [4, 4, 0, 0] },
        label: { show: cats.length <= 8, position: 'top', color: LABEL_TEXT, fontSize: 11 },
      }],
    };
  }

  private lineOption(cats: string[], vals: number[], name: string): EChartsOption {
    return {
      tooltip: { trigger: 'axis' },
      grid: { left: '3%', right: '4%', bottom: '3%', top: 28, containLabel: true },
      xAxis: { type: 'category', boundaryGap: false, data: cats, axisLine: { lineStyle: { color: AXIS_LINE } }, axisLabel: { color: AXIS_TEXT } },
      yAxis: { type: 'value', splitLine: { lineStyle: { color: SPLIT_LINE } }, axisLabel: { color: AXIS_TEXT } },
      series: [{
        name, type: 'line', data: vals, smooth: true, symbolSize: 8,
        lineStyle: { color: SERIES, width: 2 }, itemStyle: { color: SERIES },
        areaStyle: { color: 'rgba(124, 58, 237, 0.10)' },
      }],
    };
  }
}
