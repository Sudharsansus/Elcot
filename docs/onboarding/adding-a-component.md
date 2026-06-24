\
# Adding an Angular Component

This guide walks through adding a new component to the AVGC-XR Portal.

## 1. Create the Component

Use Nx to generate a component in the appropriate library:

```bash
# For a component in the UI Kit
npx nx g @nx/angular:component button-group --project=ui-kit --directory=src/lib/components

# For a feature component in the applicant portal
npx nx g @nx/angular:component scheme-status-card --project=applicant-portal --directory=src/app/features/scheme
```

## 2. Component Template

Use standalone component syntax with signals:

```typescript
import { Component, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'avgcxr-scheme-status-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './scheme-status-card.component.html',
  styleUrl: './scheme-status-card.component.scss',
})
export class SchemeStatusCardComponent {
  status = input.required<string>();
  onViewDetails = output<void>();
}
```

## 3. Use Design Tokens

Always use design system tokens, never hardcode values:

```scss
@use '@avgcxr/design-system' as ds;

:host {
  display: block;
  padding: ds.$spacing-4;
  border-radius: ds.$radius-md;
  background: ds.$color-surface;
  color: ds.$color-text-primary;
}
```

## 4. Bilingual Support

Use the TranslatePipe for all user-facing text:

```html
<h3>{{ 'scheme.status.title' | translate }}</h3>
<p>{{ 'scheme.status.description' | translate }}</p>
```

Add translations to both `en.json` and `ta.json` in the i18n library.

## 5. Accessibility

- Add `aria-label` or `aria-labelledby` to interactive elements
- Ensure keyboard navigation works (tab order, focus management)
- Test with axe DevTools
- Use semantic HTML elements

## 6. Unit Test

```typescript
import { render, screen } from '@avgcxr/testing';

describe('SchemeStatusCardComponent', () => {
  it('displays the status', () => {
    const { fixture } = render(SchemeStatusCardComponent, {
      componentInputs: { status: 'SUBMITTED' },
    });
    expect(screen.getByText('Submitted')).toBeInTheDocument();
  });
});
```

## 7. Export from UI Kit (if applicable)

Add the component to `libs/ui-kit/src/index.ts`:

```typescript
export * from './lib/components/scheme-status-card/public-api';
```
