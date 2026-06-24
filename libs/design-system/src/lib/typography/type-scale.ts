/**
 * Type Scale — Typography hierarchy for Tamil & English
 */
export const typeScale = {
  display: { fontSize: '3rem', fontWeight: 700, lineHeight: 1.2, letterSpacing: '-0.025em' },
  h1: { fontSize: '2.25rem', fontWeight: 700, lineHeight: 1.25, letterSpacing: '-0.02em' },
  h2: { fontSize: '1.875rem', fontWeight: 600, lineHeight: 1.3, letterSpacing: '-0.015em' },
  h3: { fontSize: '1.5rem', fontWeight: 600, lineHeight: 1.35, letterSpacing: '-0.01em' },
  h4: { fontSize: '1.25rem', fontWeight: 600, lineHeight: 1.4 },
  h5: { fontSize: '1.125rem', fontWeight: 500, lineHeight: 1.5 },
  h6: { fontSize: '1rem', fontWeight: 500, lineHeight: 1.5, letterSpacing: '0.01em' },
  bodyLarge: { fontSize: '1.125rem', fontWeight: 400, lineHeight: 1.75 },
  body: { fontSize: '1rem', fontWeight: 400, lineHeight: 1.5 },
  bodySmall: { fontSize: '0.875rem', fontWeight: 400, lineHeight: 1.5 },
  caption: { fontSize: '0.75rem', fontWeight: 400, lineHeight: 1.5, letterSpacing: '0.02em' },
  overline: { fontSize: '0.75rem', fontWeight: 600, lineHeight: 1.5, letterSpacing: '0.08em', textTransform: 'uppercase' as const },
  button: { fontSize: '0.875rem', fontWeight: 600, lineHeight: 1, letterSpacing: '0.02em' },
} as const;
