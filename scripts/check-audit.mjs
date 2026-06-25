#!/usr/bin/env node
// scripts/check-audit.mjs
//
// Honest dependency-audit gate for the AVGC-XR Portal.
//
// Runs `pnpm audit` and FAILS (exit 1) on ANY critical or high advisory.
// There is NO whitelist, NO ignore list, and NO deferral filter. A green
// result from this script means there are genuinely zero critical/high
// advisories in the dependency tree — it is not "zero except the known ones".
//
// (Replaces the prior version, which whitelisted @angular/*, @strapi/strapi,
//  vite and launch-editor and passed the build despite their advisories —
//  the exact "known-issues filter" forbidden by the hardening spec. See
//  docs/REMEDIATION-ROADMAP.md and docs/adr/0002-dependency-vulnerability-handling.md.)
//
// Usage:
//   node scripts/check-audit.mjs            # runs `pnpm audit --json` itself
//   node scripts/check-audit.mjs audit.json # reads a pre-captured report
import fs from 'node:fs';
import { spawnSync } from 'node:child_process';

const BLOCKING = new Set(['critical', 'high']);

function loadAuditJson() {
  const fileArg = process.argv[2];
  if (fileArg) {
    if (!fs.existsSync(fileArg)) {
      console.error(`audit file not found: ${fileArg}`);
      process.exit(2);
    }
    return fs.readFileSync(fileArg, 'utf8');
  }
  // Run pnpm audit ourselves. pnpm audit exits non-zero when it finds vulns,
  // so we capture stdout via spawnSync (which never throws on a non-zero exit)
  // instead of a shell pipeline that would need a forbidden `|| true`.
  const res = spawnSync('pnpm', ['audit', '--json'], {
    encoding: 'utf8',
    shell: process.platform === 'win32', // pnpm is a .cmd shim on Windows
    maxBuffer: 64 * 1024 * 1024,
  });
  if (res.error) {
    console.error(`failed to run pnpm audit: ${res.error.message}`);
    process.exit(2);
  }
  return res.stdout || '';
}

function parseFindings(raw) {
  let audit;
  try {
    audit = JSON.parse(raw);
  } catch {
    console.error('could not parse audit JSON output. First 500 chars:');
    console.error(raw.slice(0, 500));
    process.exit(2);
  }

  const findings = [];
  // pnpm: { advisories: { <id>: { module_name, severity, title } } }
  if (audit.advisories && typeof audit.advisories === 'object') {
    for (const adv of Object.values(audit.advisories)) {
      findings.push({
        name: adv.module_name || '(unknown)',
        severity: String(adv.severity || 'unknown').toLowerCase(),
        title: adv.title || 'see advisory',
      });
    }
    // npm: { vulnerabilities: { <pkg>: { severity, via: [{ title }] } } }
  } else if (audit.vulnerabilities && typeof audit.vulnerabilities === 'object') {
    for (const [name, info] of Object.entries(audit.vulnerabilities)) {
      const title = Array.isArray(info.via)
        ? info.via.find((v) => v && v.title)?.title
        : undefined;
      findings.push({
        name,
        severity: String(info.severity || 'unknown').toLowerCase(),
        title: title || 'see advisory',
      });
    }
  }
  return findings;
}

const findings = parseFindings(loadAuditJson());

const counts = { critical: 0, high: 0, moderate: 0, low: 0 };
for (const f of findings) {
  if (f.severity in counts) counts[f.severity] += 1;
}
const blocking = findings.filter((f) => BLOCKING.has(f.severity));

const line = '━'.repeat(64);
console.log(`\n${line}`);
console.log('  AVGC-XR Portal — Dependency Audit (honest gate, no whitelist)');
console.log(line);
console.log(
  `  critical: ${counts.critical}   high: ${counts.high}   ` +
    `moderate: ${counts.moderate}   low: ${counts.low}`,
);
console.log(line);

if (blocking.length > 0) {
  console.log(`\n  ❌ ${blocking.length} critical/high advisory(ies) — build FAILED:`);
  const order = { critical: 0, high: 1 };
  for (const f of blocking.sort((a, b) => order[a.severity] - order[b.severity])) {
    console.log(`    [${f.severity.toUpperCase().padEnd(8)}] ${f.name} — ${f.title}`);
  }
  console.log('\n  Fix: upgrade the affected package to a patched version, or pin a fixed');
  console.log('  transitive version via package.json "pnpm.overrides".');
  console.log('  There is NO deferral whitelist. See docs/REMEDIATION-ROADMAP.md.\n');
  process.exit(1);
}

console.log('\n  ✅ Zero critical/high advisories.\n');
process.exit(0);
