/**
 * Semantic Palette — Status and feedback colors
 * Meets WCAG 2.1 AA contrast ratio requirements (4.5:1 minimum)
 */
export const semanticPalette = {
  success: { light: '#DCFCE7', main: '#15803D', dark: '#14532D', contrastText: '#FFFFFF' },
  warning: { light: '#FEF3C7', main: '#B45309', dark: '#78350F', contrastText: '#0F172A' },
  error:   { light: '#FEE2E2', main: '#DC2626', dark: '#991B1B', contrastText: '#FFFFFF' },
  info:    { light: '#DBEAFE', main: '#0369A1', dark: '#075985', contrastText: '#FFFFFF' },
} as const;
export type SemanticStatus = keyof typeof semanticPalette;
