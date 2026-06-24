/**
 * Responsive Breakpoint Tokens — Mobile-first, GIGW compliant
 */
export const breakpointTokens = {
  xs: '320px', sm: '576px', md: '768px', lg: '992px', xl: '1200px', '2xl': '1440px', '3xl': '1920px',
  mobile: '576px', tablet: '768px', desktop: '992px', wide: '1200px', ultrawide: '1440px',
} as const;

export const mediaQueries = {
  xs: `@media (min-width: ${breakpointTokens.xs})`,
  sm: `@media (min-width: ${breakpointTokens.sm})`,
  md: `@media (min-width: ${breakpointTokens.md})`,
  lg: `@media (min-width: ${breakpointTokens.lg})`,
  xl: `@media (min-width: ${breakpointTokens.xl})`,
  mobile: `@media (max-width: ${breakpointTokens.sm})`,
  tablet: `@media (min-width: ${breakpointTokens.sm}) and (max-width: ${breakpointTokens.lg})`,
  desktop: `@media (min-width: ${breakpointTokens.lg})`,
  print: '@media print',
  reducedMotion: '@media (prefers-reduced-motion: reduce)',
} as const;
