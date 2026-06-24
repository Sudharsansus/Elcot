\
# Visual Regression Tests

Visual regression tests catch unintended UI changes by comparing screenshots across builds.

## Approach

Using Playwright for visual comparison. Baseline screenshots are stored in `tests/visual/baselines/`.

## Running

```bash
# Update baselines (first run or after intentional design change)
npx nx run visual:update --project=public-portal

# Compare against baselines
npx nx run visual:compare --project=public-portal
```

## Components Tested

- All 13 UI Kit components
- Key portal pages (home, scheme list, scheme detail, application form)
- Both English and Tamil language variants
- Mobile and desktop viewports
- Light and dark themes (when applicable)

## Threshold

Maximum pixel difference: 0.5% (allows for anti-aliasing and font rendering differences).
