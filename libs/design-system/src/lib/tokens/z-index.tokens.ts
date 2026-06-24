/**
 * Z-Index Layer Tokens — Prevents stacking conflicts
 */
export const zIndexTokens = {
  base: 0, dropdown: 1000, sticky: 1020, fixed: 1030, drawer: 1040,
  modalBackdrop: 1050, modal: 1060, popover: 1070, tooltip: 1080, toast: 1090,
  skipLink: 1100,
} as const;
