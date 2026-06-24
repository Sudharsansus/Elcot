\
# Accessibility Tests

Automated accessibility testing to verify WCAG 2.1 AA compliance.

## Tools

| Tool | Type | Runs In |
|------|------|---------|
| Axe DevTools | Automated | CI pipeline, browser |
| Lighthouse | Automated | CI pipeline |
| jest-axe | Automated | Jest unit tests |
| NVDA | Manual | Monthly audit |

## Running

```bash
# Automated (CI)
npx nx run accessibility:audit --project=public-portal

# In browser
# Open DevTools → Axe DevTools extension → Scan

# Lighthouse
npx lighthouse http://localhost:4200 --only-categories=accessibility --output=html --output-path=reports/a11y.html
```

## Requirements

- Zero critical/serious violations (axe)
- Lighthouse accessibility score >= 90
- All images have alt text
- All form inputs have labels
- Color contrast ratio >= 4.5:1
- All interactive elements keyboard accessible
- Skip-to-content link present
- Page language correctly set (en/ta)
