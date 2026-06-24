# ADR-0013: Apache ECharts for Data Visualization

## Status

Accepted

## Date

2026-02-05

## Context

The admin and applicant dashboards require interactive data visualizations: application status pie charts, district-wise bar charts, trend lines, and scheme comparison charts. The library must support responsive design, accessibility (WCAG 2.1 AA), Tamil language labels, and export to PNG/PDF for government reports.

## Decision

Use Apache ECharts 5 as the charting library. ECharts provides a comprehensive chart type library (bar, line, pie, scatter, heatmap, treemap, sankey), built-in responsiveness, theme support, and accessibility features including ARIA labels and keyboard navigation. The `ngx-echarts` Angular wrapper integrates with Angular's change detection and supports OnPush strategy.

## Consequences

### Positive

- ECharts supports 20+ chart types out of the box, covering all dashboard requirements.
- Built-in responsive mode automatically resizes charts on viewport changes.
- Theme system allows custom AVGC-XR theme matching the design system tokens.
- ARIA attributes and keyboard navigation support WCAG 2.1 AA compliance.
- Canvas-based rendering handles large datasets (10,000+ data points) smoothly.
- Tamil text rendering works correctly with Noto Sans SC font.

### Negative

- ECharts bundle size is ~1MB (minified), though tree-shaking with ngx-echarts reduces this to ~300KB per chart type.
- Canvas rendering is not ideal for print; SVG mode is available but slower for large datasets.
- Learning curve for custom chart configurations.

### Risks

- Risk: Large bundle size affects initial portal load time. Mitigation: Use dynamic imports for chart components; configure Nx to tree-shake unused ECharts modules.