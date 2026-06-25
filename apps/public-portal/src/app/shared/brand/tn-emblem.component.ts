// ============================================================
// TAMIL NADU GOVERNMENT EMBLEM — inline, themeable SVG seal
// ============================================================
// A dignified rendition of the Tamil Nadu state emblem motif: a South-Indian
// temple gopuram flanked by two lamps inside a double-ring seal. Drawn with
// `currentColor` so it inherits its colour from context (white on the dark
// masthead, TN-red on light surfaces). Inlined (not an <img>) so it is
// SSR-safe, needs no extra HTTP request, and stays crisp at any size.
//
// NOTE: This is a respectful interpretation for the portal crest. On a real
// ELCOT deployment, swap in the official Government of Tamil Nadu emblem asset.
// ============================================================

import { Component, ChangeDetectionStrategy, Input } from '@angular/core';

let _emblemSeq = 0;

@Component({
  selector: 'app-tn-emblem',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <svg
      [attr.width]="size"
      [attr.height]="size"
      viewBox="0 0 200 200"
      fill="none"
      [attr.role]="decorative ? 'presentation' : 'img'"
      [attr.aria-hidden]="decorative ? 'true' : null"
      [attr.aria-label]="decorative ? null : 'Government of Tamil Nadu emblem'"
      class="tn-emblem"
    >
      <defs>
        <path [attr.id]="ringTopId" d="M 100,100 m -82,0 a 82,82 0 1,1 164,0" />
        <path [attr.id]="ringBottomId" d="M 100,100 m 82,0 a 82,82 0 1,1 -164,0" />
      </defs>

      <!-- Double ring seal -->
      <circle cx="100" cy="100" r="96" stroke="currentColor" stroke-width="2.5" />
      <circle cx="100" cy="100" r="78" stroke="currentColor" stroke-width="1.5" opacity="0.65" />

      <!-- Gopuram (temple tower): stacked tapering tiers -->
      <g stroke="currentColor" stroke-width="2.4" stroke-linejoin="round" fill="none">
        <!-- base platform -->
        <path d="M62 138 H138 V146 H62 Z" />
        <!-- tier 1 -->
        <path d="M66 138 L72 124 H128 L134 138 Z" />
        <!-- tier 2 -->
        <path d="M73 124 L78 112 H122 L127 124 Z" />
        <!-- tier 3 -->
        <path d="M79 112 L84 101 H116 L121 112 Z" />
        <!-- tier 4 -->
        <path d="M85 101 L90 91 H110 L115 101 Z" />
        <!-- shrine / kudu arch -->
        <path d="M91 91 L100 78 L109 91 Z" />
        <!-- doorway -->
        <path d="M94 138 V120 Q100 114 106 120 V138" stroke-width="2" />
      </g>

      <!-- Kalasam finials on top -->
      <g fill="currentColor">
        <circle cx="100" cy="73" r="3" />
        <circle cx="92" cy="86" r="1.8" />
        <circle cx="108" cy="86" r="1.8" />
      </g>

      <!-- Flanking oil lamps (deepam) -->
      <g stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round">
        <!-- left -->
        <path d="M44 132 H58" />
        <path d="M51 132 V120" />
        <path d="M45 118 Q51 124 57 118 Z" />
        <!-- right -->
        <path d="M142 132 H156" />
        <path d="M149 132 V120" />
        <path d="M143 118 Q149 124 155 118 Z" />
      </g>
      <!-- lamp flames -->
      <g fill="currentColor">
        <path d="M51 118 q-2 -4 0 -7 q2 3 0 7 Z" />
        <path d="M149 118 q-2 -4 0 -7 q2 3 0 7 Z" />
      </g>

      @if (ring) {
        <text class="tn-emblem__ring" fill="currentColor" font-size="13.5" font-weight="700" letter-spacing="2.5">
          <textPath [attr.href]="'#' + ringTopId" startOffset="50%" text-anchor="middle">GOVERNMENT OF TAMIL NADU</textPath>
        </text>
        <text class="tn-emblem__ring tn-emblem__ring--ta" fill="currentColor" font-size="14" font-weight="600">
          <textPath [attr.href]="'#' + ringBottomId" startOffset="50%" text-anchor="middle">தமிழ்நாடு அரசு</textPath>
        </text>
      }
    </svg>
  `,
  styles: [`
    :host { display: inline-flex; line-height: 0; }
    .tn-emblem { display: block; }
    .tn-emblem__ring { font-family: 'Noto Serif', Georgia, serif; }
    .tn-emblem__ring--ta { font-family: 'Noto Sans Tamil', sans-serif; }
  `]
})
export class TnEmblemComponent {
  /** Pixel size of the square seal. */
  @Input() size = 48;
  /** Render the circular ring text ("Government of Tamil Nadu / தமிழ்நாடு அரசு"). */
  @Input() ring = false;
  /** When true, the seal is purely decorative (aria-hidden). */
  @Input() decorative = false;

  private readonly uid = `tnemb-${++_emblemSeq}`;
  readonly ringTopId = `${this.uid}-top`;
  readonly ringBottomId = `${this.uid}-bot`;
}
