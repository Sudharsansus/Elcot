\
# Codemods

Automated code transformations for the AVGC-XR Portal codebase.

## Available Codemods

| Codemod | Purpose |
|---------|---------|
| `rename-import.ts` | Update import paths after library restructuring |
| `add-i18n-keys.ts` | Extract hardcoded strings to i18n keys |
| `migrate-to-signals.ts` | Convert BehaviorSubject to Angular Signals |
| `add-a11y-attrs.ts` | Add missing ARIA attributes to components |

## Running

```bash
# Using ts-node
npx ts-node tools/codemods/add-i18n-keys.ts --path="apps/public-portal/src"

# Using jscodeshift
npx jscodeshift -t tools/codemods/migrate-to-signals.ts apps/
```

## Writing a New Codemod

Use jscodeshift for TypeScript/JavaScript transformations:

```typescript
import { Transform } from 'jscodeshift';

const transform: Transform = (file, api) => {
  const j = api.jscodeshift;
  const root = j(file.source);
  // Your transformation logic here
  return root.toSource();
};

export default transform;
```
