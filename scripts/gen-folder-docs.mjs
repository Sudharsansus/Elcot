#!/usr/bin/env node
// ============================================================
// FOLDER DOCS GENERATOR — knowledge transfer for the AVGC-XR portal.
//
// Writes a README.md into every source folder describing:
//   • the folder's purpose (from path conventions),
//   • each file it contains + what that file does (extracted from the file's
//     own header comment / docstring, with sensible fallbacks by type),
//   • its subfolders.
//
// Non-destructive: never overwrites a hand-written README.md (skips folders that
// already have one). Re-run any time:  node scripts/gen-folder-docs.mjs
//
// Excludes dependency/build artifacts (node_modules, .git, dist, target, .nx,
// .angular) so it documents only the real project (~1,093 folders).
// ============================================================
import { readdirSync, statSync, writeFileSync, existsSync, readFileSync } from 'node:fs';
import { join, relative, extname } from 'node:path';

const ROOT = process.cwd();
const PRUNE = new Set(['node_modules', '.git', 'dist', 'target', '.nx', '.angular', 'coverage']);
const DOC_EXT = new Set(['.ts', '.js', '.mjs', '.cjs', '.java', '.scss', '.css', '.html']);
const GENERATED_MARK = '<!-- gen-folder-docs -->';

let written = 0, skipped = 0, regenerated = 0;

const norm = (p) => p.split('\\').join('/');
const clean = (s) => s.replace(/[=]{3,}|[-]{4,}|[*]{3,}/g, ' ').replace(/\s+/g, ' ').trim();

/** Pull the leading comment/docstring from a source file as its description. */
function headerDoc(file) {
  let text;
  try { text = readFileSync(file, 'utf8'); } catch { return ''; }
  const ext = extname(file);
  if (ext === '.html') {
    const m = text.match(/<!--([\s\S]*?)-->/);
    return m ? cap(clean(m[1])) : '';
  }
  const lines = text.split(/\r?\n/);
  let i = 0;
  while (i < lines.length && (lines[i].trim() === '' || lines[i].startsWith('#!'))) i++;
  if (i >= lines.length) return '';
  const first = lines[i].trimStart();
  if (first.startsWith('/*')) {
    const buf = [];
    for (let j = i; j < lines.length; j++) { buf.push(lines[j]); if (lines[j].includes('*/')) break; }
    return cap(clean(buf.join('\n').replace(/\/\*+|\*+\//g, '').replace(/^\s*\*/gm, ''))); // strip /* */ and leading *
  }
  if (first.startsWith('//')) {
    const buf = [];
    for (let j = i; j < lines.length && lines[j].trimStart().startsWith('//'); j++) {
      buf.push(lines[j].replace(/^\s*\/\/\s?/, ''));
    }
    return cap(clean(buf.join(' ')));
  }
  return '';
}

function cap(s, n = 260) {
  s = s.replace(/\|/g, '/').trim();
  if (s.length <= n) return s;
  const cut = s.slice(0, n);
  const dot = cut.lastIndexOf('. ');
  return (dot > 60 ? cut.slice(0, dot + 1) : cut) + ' …';
}

/** Fallback description by file name / extension when there is no header. */
function fallbackDesc(name, ext) {
  const n = name.toLowerCase();
  if (n === 'package.json') return 'NPM package manifest — dependencies, scripts and metadata.';
  if (n === 'project.json') return 'Nx project configuration — build/serve/test targets.';
  if (n.startsWith('tsconfig')) return 'TypeScript compiler configuration.';
  if (n === 'jest.config.ts' || n === 'jest.config.js') return 'Jest test-runner configuration.';
  if (n === 'karma.conf.js') return 'Karma test-runner configuration.';
  if (n.endsWith('.spec.ts') || n.endsWith('.spec.js')) return 'Unit test specification for the matching source file.';
  if (n === 'index.ts' || n === 'index.js') return 'Barrel file — re-exports this folder’s public API.';
  if (n === '.gitkeep') return 'Placeholder so Git tracks an otherwise-empty folder.';
  if (n === 'dockerfile') return 'Container build recipe for this service.';
  if (n.startsWith('application') && n.endsWith('.yml')) return 'Spring Boot application configuration.';
  if (n.endsWith('.pom') || n === 'pom.xml') return 'Maven build configuration (module dependencies + plugins).';
  const map = {
    '.png': 'Raster image asset.', '.jpg': 'Raster image asset.', '.jpeg': 'Raster image asset.',
    '.svg': 'Vector graphic asset.', '.ico': 'Icon asset.', '.webp': 'Raster image asset.', '.gif': 'Image asset.',
    '.woff2': 'Web font (WOFF2).', '.woff': 'Web font (WOFF).', '.ttf': 'Font file.', '.eot': 'Font file.',
    '.json': 'JSON data / configuration.', '.yml': 'YAML configuration.', '.yaml': 'YAML configuration.',
    '.md': 'Markdown documentation.', '.txt': 'Plain-text file.', '.properties': 'Java properties configuration.',
    '.xml': 'XML configuration / data.', '.html': 'HTML template or page.', '.scss': 'Sass stylesheet.',
    '.css': 'Stylesheet.', '.sql': 'SQL script (schema or migration).', '.sh': 'Shell script.',
    '.mjs': 'JavaScript module.', '.js': 'JavaScript source.', '.ts': 'TypeScript source.', '.java': 'Java source.',
    '.jsonl': 'JSON-Lines data (one JSON object per line).', '.env': 'Environment variables.',
  };
  return map[ext] || 'Project file.';
}

const kindOf = (ext) => ({
  '.ts': 'TypeScript', '.js': 'JavaScript', '.mjs': 'JS module', '.cjs': 'JS module', '.java': 'Java',
  '.scss': 'Sass', '.css': 'CSS', '.html': 'HTML', '.json': 'JSON', '.yml': 'YAML', '.yaml': 'YAML',
  '.md': 'Markdown', '.svg': 'SVG', '.png': 'PNG', '.jpg': 'JPEG', '.ico': 'Icon', '.woff2': 'Font',
  '.properties': 'Properties', '.xml': 'XML', '.sql': 'SQL', '.jsonl': 'JSONL',
}[ext] || (ext ? ext.slice(1).toUpperCase() : 'file'));

/** Human purpose of a folder from its path (last segment + a few known names). */
function folderPurpose(rel) {
  if (rel === '.') return 'Repository root — the Tamil Nadu AVGC-XR portal (Nx monorepo: 3 Angular portals + Spring Boot backend + shared libraries).';
  const parts = rel.split('/');
  const seg = parts[parts.length - 1].toLowerCase();
  const byName = {
    services: 'Injectable singleton services — business logic, API access and shared state.',
    guards: 'Angular route guards (authentication / role authorization).',
    interceptors: 'HTTP interceptors — auth token, error handling, tracing, response unwrapping.',
    components: 'Reusable UI components.',
    pages: 'Routed page components (one per screen).',
    features: 'Feature modules — each subfolder is a self-contained feature.',
    core: 'Core singletons used app-wide: services, guards, interceptors, tokens.',
    shared: 'Shared, reusable building blocks (components, pipes, directives).',
    pipes: 'Angular pipes (value transforms used in templates).',
    directives: 'Angular directives (attribute behaviour on elements).',
    models: 'Data-model / DTO type definitions.',
    tokens: 'Angular dependency-injection tokens.',
    assets: 'Static assets served as-is (images, fonts, icons).',
    environments: 'Build-time environment configuration (dev vs prod endpoints).',
    chat: 'The Mira / AI Mode assistant feature (public portal).',
    'ai-mode': 'AI Mode — the command-palette AI interaction surface.',
    reports: 'AI Reporting — natural-language analytics for officials.',
    dashboard: 'Dashboard feature (landing screen after sign-in).',
    applications: 'Applications feature — apply / review / track.',
    workflow: 'Workflow / process-management feature.',
    users: 'User-management feature (admin).',
    auth: 'Authentication feature — login, register, password reset.',
    profile: 'User profile feature.',
    schemes: 'Incentive-schemes feature (catalogue, detail, finder).',
    companies: 'Business Connect — company/studio directory.',
    talent: 'Talent Connect — professionals registry.',
    freelancers: 'Freelancer registry feature.',
    events: 'News & Events feature.',
    resources: 'Resources & policy-documents feature.',
    about: 'About / static informational pages.',
    home: 'Home / landing feature.',
    ml: 'Offline AI model assets — fine-tune dataset, eval gate and integration docs (no API keys).',
    finetune: 'Fine-tuning dataset + instructions for the offline model.',
    eval: 'Held-out evaluation set — the go-live quality gate for the model.',
    infra: 'Infrastructure — container/compose and deployment configuration.',
    docs: 'Project documentation.',
    scripts: 'Developer / operational scripts.',
    tools: 'Repo tooling and generators.',
    tests: 'End-to-end / integration tests.',
    observability: 'Observability configuration (metrics, logs, tracing).',
    migration: 'Flyway database migrations.',
    resources_: 'Backend resources (config, static, templates).',
  };
  if (byName[seg]) return byName[seg];
  if (parts.length === 2 && parts[0] === 'apps') return `The **${parts[1]}** application.`;
  if (parts.length === 2 && parts[0] === 'libs') return `Shared library: **${parts[1]}** (imported as @avgc-xr/${parts[1]} across the apps).`;
  if (seg === 'src') return 'Source root for this project.';
  if (seg === 'lib') return 'Library source (the exported implementation).';
  if (seg === 'main') return 'Main source set (production code).';
  if (seg === 'test') return 'Test source set.';
  if (seg === 'java') return 'Java package root.';
  if (seg === 'in' || seg === 'elcot' || seg === 'avgcxr') return 'Java package segment (in.elcot.avgcxr — the backend base package).';
  return `Folder holding the files listed below (part of \`${parts.slice(0, -1).join('/') || 'the project'}\`).`;
}

function listDir(dir) {
  const files = [], dirs = [];
  for (const name of readdirSync(dir)) {
    if (PRUNE.has(name)) continue;
    const full = join(dir, name);
    let st;
    try { st = statSync(full); } catch { continue; }
    if (st.isDirectory()) dirs.push(name);
    else if (name !== 'README.md') files.push(name);
  }
  return { files: files.sort(), dirs: dirs.sort() };
}

const titled = (s) => s.replace(/[-_.]/g, ' ').replace(/([a-z0-9])([A-Z])/g, '$1 $2').replace(/\s+/g, ' ').trim().replace(/\b\w/g, (c) => c.toUpperCase());

/** Description inferred from Angular/Java naming conventions (used when a file has no header comment). */
function nameBasedDesc(name, ext) {
  const n = name.toLowerCase();
  if (ext === '.ts' || ext === '.js') {
    const ng = n.match(/^(.*)\.(service|component|guard|interceptor|pipe|directive|routes|module|resolver|store|facade|token|config|model|dto)\.(ts|js)$/);
    if (ng) {
      const t = titled(ng[1]);
      const m = {
        service: `${t} service — injectable providing business logic, state and/or API access.`,
        component: `${t} — UI component (template + logic for a screen or widget).`,
        guard: `${t} route guard — controls access to routes (auth / role).`,
        interceptor: `${t} HTTP interceptor — cross-cutting request/response handling.`,
        pipe: `${t} pipe — transforms a value inside templates.`,
        directive: `${t} directive — attribute behaviour applied to elements.`,
        routes: `Route definitions for the ${t} area (lazy-loaded screens).`,
        module: `${t} Angular module.`,
        resolver: `${t} route resolver — pre-fetches data before a route activates.`,
        store: `${t} state store.`,
        facade: `${t} facade — the feature's public API surface.`,
        token: `${t} dependency-injection token.`,
        config: `${t} configuration.`,
        model: `${t} — data model / TypeScript type definitions.`,
        dto: `${t} — data transfer object (API payload shape).`,
      };
      return m[ng[2]] || '';
    }
  }
  if (ext === '.java') {
    const base = name.replace(/\.java$/, '');
    const t = titled(base);
    if (/Controller$/.test(base)) return `${t} — REST controller (defines HTTP endpoints).`;
    if (/Service$/.test(base)) return `${t} — service (business logic).`;
    if (/(Repository|Repo|Adapter|Port)$/.test(base)) return `${t} — data-access / port (persistence or external boundary).`;
    if (/(Config|Configuration)$/.test(base)) return `${t} — Spring configuration.`;
    if (/(Request|Response|Dto)$/.test(base)) return `${t} — data transfer object (API payload).`;
    if (/Entity$/.test(base)) return `${t} — JPA entity (maps to a database table).`;
    if (/Exception$/.test(base)) return `${t} — exception type.`;
    if (/Mapper$/.test(base)) return `${t} — mapper (entity ↔ DTO conversion).`;
    if (/(Filter|Provider|Handler|Factory|Validator|Converter|Listener|Scheduler|Job|Aspect|Guard|Interceptor)$/.test(base)) {
      return `${t} — ${(base.match(/[A-Z][a-z]+$/) || ['component'])[0].toLowerCase()} component.`;
    }
    if (/Application$/.test(base)) return `${t} — Spring Boot application entry point.`;
    return `${t} — Java class.`;
  }
  return '';
}

function describe(dir, name) {
  const ext = extname(name).toLowerCase();
  let desc = DOC_EXT.has(ext) ? headerDoc(join(dir, name)) : '';
  if (!desc) desc = nameBasedDesc(name, ext);
  if (!desc) desc = fallbackDesc(name, ext);
  return { kind: kindOf(ext), desc };
}

function buildReadme(dir) {
  const rel = norm(relative(ROOT, dir)) || '.';
  const { files, dirs } = listDir(dir);
  const title = rel === '.' ? 'Repository root' : rel;
  let md = `# \`${title}\`\n\n${GENERATED_MARK}\n`;
  md += `> Folder guide for knowledge transfer — auto-generated from each file's own header documentation.\n`;
  md += `> Regenerate with \`node scripts/gen-folder-docs.mjs\`. Do not edit by hand (changes are overwritten); to keep a hand-written note, remove the \`${GENERATED_MARK}\` marker line.\n\n`;
  md += `**Purpose:** ${folderPurpose(rel)}\n\n`;

  if (files.length) {
    md += `## Files (${files.length})\n\n| File | Type | What it does |\n| --- | --- | --- |\n`;
    for (const f of files) {
      const { kind, desc } = describe(dir, f);
      md += `| \`${f}\` | ${kind} | ${desc || '—'} |\n`;
    }
    md += `\n`;
  } else {
    md += `_No files directly in this folder (see subfolders below)._\n\n`;
  }

  if (dirs.length) {
    md += `## Subfolders (${dirs.length})\n\n`;
    for (const d of dirs) md += `- [\`${d}/\`](${d}/README.md) — ${folderPurpose((rel === '.' ? '' : rel + '/') + d)}\n`;
    md += `\n`;
  }
  md += `---\n_Part of the Tamil Nadu AVGC-XR portal. This guide describes files as documented in their source headers; for authoritative behaviour, read the code._\n`;
  return md;
}

function walk(dir) {
  const readmePath = join(dir, 'README.md');
  let existing = null;
  if (existsSync(readmePath)) { try { existing = readFileSync(readmePath, 'utf8'); } catch { /* */ } }
  // Skip hand-written READMEs; only (re)write ones we generated.
  if (existing !== null && !existing.includes(GENERATED_MARK)) {
    skipped++;
  } else {
    writeFileSync(readmePath, buildReadme(dir), 'utf8');
    existing !== null ? regenerated++ : written++;
  }
  for (const name of readdirSync(dir)) {
    if (PRUNE.has(name)) continue;
    const full = join(dir, name);
    try { if (statSync(full).isDirectory()) walk(full); } catch { /* */ }
  }
}

walk(ROOT);
console.log(`Folder docs: ${written} created, ${regenerated} regenerated, ${skipped} skipped (hand-written README preserved).`);
