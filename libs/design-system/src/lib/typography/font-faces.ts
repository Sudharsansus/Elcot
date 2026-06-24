/**
 * Font Face Declarations — Noto Sans (EN) + Noto Sans Tamil
 * Fallback to system fonts for offline/intranet deployments
 */
export const fontFaceCSS = `
@font-face {
  font-family: 'Noto Sans';
  font-style: normal; font-weight: 400; font-display: swap;
  src: url('https://fonts.gstatic.com/s/notosans/v36/o-0mIpQlx3QUlC5A4PNR6Ryti20_6n1iPHjcz6L1SoM-jCpoiyD9A-9a6Vjki0.woff2') format('woff2');
  unicode-range: U+0000-00FF, U+0131, U+0152-0153, U+02BB-02BC, U+02C6, U+02DA, U+02DC, U+200C-200D, U+20B9, U+25CC;
}
@font-face {
  font-family: 'Noto Sans'; font-style: normal; font-weight: 600; font-display: swap;
  src: url('https://fonts.gstatic.com/s/notosans/v36/o-0mIpQlx3QUlC5A4PNR6Ryti20_6n1iPHjcz6L1SoM-jCpoiyD9A-9a6Vjki0.woff2') format('woff2');
  unicode-range: U+0000-00FF, U+0131, U+0152-0153, U+02BB-02BC, U+02C6, U+02DA, U+02DC, U+200C-200D, U+20B9, U+25CC;
}
@font-face {
  font-family: 'Noto Sans Tamil'; font-style: normal; font-weight: 400; font-display: swap;
  src: url('https://fonts.gstatic.com/s/notosanstamil/v27/piEyn8S5pKxN428ai2P6dVU0ml6b2zA.woff2') format('woff2');
  unicode-range: U+0964-0965, U+0B80-0BFF, U+200C-200D, U+20B9, U+25CC;
}
`;

export const tamilFontFaceCSS = `
@font-face {
  font-family: 'Noto Sans Tamil'; font-style: normal; font-weight: 400; font-display: swap;
  src: url('https://fonts.gstatic.com/s/notosanstamil/v27/piEyn8S5pKxN428ai2P6dVU0ml6b2zA.woff2') format('woff2');
  unicode-range: U+0964-0965, U+0B80-0BFF, U+200C-200D, U+20B9, U+25CC;
}
`;
