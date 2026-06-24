\
# WCAG 2.1 AA Compliance Evidence

## Accessibility Standards

The AVGC-XR Portal is designed to meet **WCAG 2.1 Level AA** compliance as required by the Rights of Persons with Disabilities Act 2016 and GIGW 3.0 guidelines.

## Perceivable

| Criterion | Implementation | Tested |
|-----------|---------------|--------|
| 1.1.1 Non-text Content | All images have `alt` text in both EN and TA | Yes |
| 1.2.2 Captions (Prerecorded) | Video content includes Tamil and English subtitles | Yes |
| 1.3.1 Info and Relationships | Semantic HTML5 elements, ARIA landmarks | Yes |
| 1.3.2 Meaningful Sequence | DOM order matches visual order | Yes |
| 1.4.1 Use of Color | Information not conveyed by color alone | Yes |
| 1.4.3 Contrast (Minimum) | 4.5:1 for normal text, 3:1 for large text | Yes |
| 1.4.4 Resize Text | Text scales up to 200% without loss of content | Yes |
| 1.4.11 Non-text Contrast | UI components have 3:1 contrast ratio | Yes |

## Operable

| Criterion | Implementation | Tested |
|-----------|---------------|--------|
| 2.1.1 Keyboard | All interactive elements keyboard accessible | Yes |
| 2.1.2 No Keyboard Trap | Focus can move to and from all components | Yes |
| 2.2.1 Timing Adjustable | No time limits on form submission | Yes |
| 2.4.1 Bypass Blocks | Skip-to-content link on every page | Yes |
| 2.4.2 Page Titled | Descriptive page titles in current language | Yes |
| 2.4.3 Focus Order | Logical tab order following visual layout | Yes |
| 2.4.7 Focus Visible | Visible focus indicators on all interactive elements | Yes |

## Understandable

| Criterion | Implementation | Tested |
|-----------|---------------|--------|
| 3.1.1 Language of Page | `lang` attribute set to `en` or `ta` | Yes |
| 3.2.1 On Focus | No context change on focus | Yes |
| 3.2.2 On Input | No unexpected context changes on input | Yes |
| 3.3.1 Error Identification | Form errors identified and described in text | Yes |
| 3.3.2 Labels | All form inputs have visible labels | Yes |

## Robust

| Criterion | Implementation | Tested |
|-----------|---------------|--------|
| 4.1.1 Parsing | Valid HTML5, no duplicate IDs | Yes |
| 4.1.2 Name, Role, Value | ARIA attributes on custom components | Yes |

## Testing Tools

- Axe DevTools (automated) - integrated in CI pipeline
- NVDA screen reader (manual) - tested monthly
- Keyboard-only navigation (manual) - tested per sprint
- Lighthouse accessibility audit - CI gate at 90+ score
