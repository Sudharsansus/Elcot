export const colorTokens = {
  primary: { 50: '#EFF6FF', 100: '#DBEAFE', 200: '#BFDBFE', 300: '#93C5FD', 400: '#60A5FA', 500: '#0D47A1', 600: '#0B3D8E', 700: '#09337B', 800: '#072968', 900: '#051F55' },
  secondary: { 50: '#FFF7ED', 500: '#FF6F00' },
  neutral: { 0: '#FFFFFF', 50: '#F8FAFC', 900: '#0F172A' },
  success: '#15803D', warning: '#B45309', error: '#DC2626', info: '#0369A1',
} as const;
export type ColorToken = typeof colorTokens;
