#!/usr/bin/env node
// scripts/check-audit.mjs
// Security audit filter for the AVGC-XR Portal.
// Fails on CRITICAL/HIGH vulns in non-whitelisted packages.
// Passes on whitelisted packages (Y2 hardening plan covers them).
// Updated: 2026-06-24
import fs from 'node:fs';
import path from 'node:path';

const auditPath = process.argv[2] || 'audit.json';
if (!fs.existsSync(auditPath)) {
  console.error(`❌ Audit file not found: ${auditPath}`);
  console.error('   Run: pnpm audit --json > audit.json');
  process.exit(2);
}

const audit = JSON.parse(fs.readFileSync(auditPath, 'utf8'));

// pnpm audit --json emits top-level "advisories" keyed by GHSA ID,
// with each entry holding {module_name, severity, title, ...}.
// npm audit --json emits top-level "vulnerabilities" keyed by package
// name with {severity, via: [{title}], ...}. Support both.
let vulns = {};
if (audit.advisories && typeof audit.advisories === 'object') {
  // pnpm format: re-key by module_name for the filter logic
  for (const adv of Object.values(audit.advisories)) {
    const name = adv.module_name;
    if (!name) continue;
    vulns[name] = {
      severity: adv.severity,
      via: [{ title: adv.title }],
    };
  }
} else if (audit.vulnerabilities && typeof audit.vulnerabilities === 'object') {
  vulns = audit.vulnerabilities;
}

// Whitelisted packages: known issues requiring major framework upgrade
// See SECURITY_KNOWN_ISSUES.md and SCALING-ROADMAP.md (Y2 Hardening section)
const KNOWN_PACKAGES = [
  // Angular: requires 17→19 major upgrade (~3 weeks work)
  '@angular/core', '@angular/compiler', '@angular/common',
  '@angular/platform-server', '@angular/ssr', '@angular/router',
  '@angular/forms', '@angular/animations', '@angular/material',
  '@angular/cdk', '@angular/elements', '@angular/upgrade',
  '@angular/compiler-cli', '@angular/build', '@angular-devkit/build-angular',
  // Strapi: requires 4→5 major upgrade (API changes)
  '@strapi/strapi',
  // Vite: requires 5→6 major upgrade (Angular 17 incompatible with 6)
  'vite',
  // Transitive of vite 5.x
  'launch-editor',
];

let criticalCount = 0, highCount = 0, moderateCount = 0, lowCount = 0;
const unexpected = [];
const known = [];

for (const [pkgName, info] of Object.entries(vulns)) {
  const sev = info.severity || 'unknown';
  if (sev === 'critical') criticalCount++;
  else if (sev === 'high') highCount++;
  else if (sev === 'moderate') moderateCount++;
  else if (sev === 'low') lowCount++;

  if (sev !== 'critical' && sev !== 'high') continue;

  const isKnown = KNOWN_PACKAGES.some(p => pkgName === p || pkgName.startsWith(p + '@'));
  if (isKnown) {
    known.push(`${sev.toUpperCase().padEnd(8)} ${pkgName}`);
  } else {
    unexpected.push(`${sev.toUpperCase().padEnd(8)} ${pkgName} (${info.via?.[0]?.title || 'see audit'})`);
  }
}

console.log('');
console.log('━'.repeat(64));
console.log('  AVGC-XR Portal — Security Audit');
console.log('━'.repeat(64));
console.log(`  Total vulns:      ${Object.keys(vulns).length}`);
console.log(`  Critical:         ${criticalCount}`);
console.log(`  High:             ${highCount}`);
console.log(`  Moderate:         ${moderateCount} (informational)`);
console.log(`  Low:              ${lowCount} (informational)`);
console.log('');
console.log(`  Known (Y2 plan):  ${known.length}`);
console.log(`  Unexpected:       ${unexpected.length}`);
console.log('━'.repeat(64));

if (known.length > 0) {
  console.log('');
  console.log('  Known issues (deferred to Year 2 hardening):');
  for (const v of known.slice(0, 10)) console.log(`    [OK]    ${v}`);
  if (known.length > 10) console.log(`    [OK]    ... and ${known.length - 10} more`);
  console.log('  See SECURITY_KNOWN_ISSUES.md and SCALING-ROADMAP.md');
}

if (unexpected.length > 0) {
  console.log('');
  console.log('  ❌ UNEXPECTED VULNS — build FAILED:');
  for (const v of unexpected) console.log(`    [FAIL]  ${v}`);
  console.log('');
  console.log('  Action: pin to fixed version via .npmrc override, or');
  console.log('          add to KNOWN_PACKAGES list with justification.');
  process.exit(1);
}

console.log('');
console.log('  ✅ All critical/high vulns are tracked in Y2 hardening plan.');
console.log('     New critical/high vulns in other packages will fail the build.');
console.log('');
process.exit(0);
