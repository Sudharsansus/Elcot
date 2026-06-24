// apps/admin-portal/webpack.config.js
// Custom webpack config for @angular-builders/custom-webpack:browser.
// Registers a Sass `pkg:`-style importer that resolves bare package
// names (e.g. `@material/typography`, `@angular/material`) from
// node_modules. This is required because @angular/material 17.3's
// internal SCSS uses `@use '@material/typography'` which Dart Sass
// 1.71+ cannot resolve without an explicit importer.

const path = require('path');
const fs = require('fs');

const NODE_MODULES = path.resolve(__dirname, '..', '..', 'node_modules');

function resolvePackageToScss(packageUrl) {
  // Only attempt to resolve URL-looking package specifiers.
  if (!packageUrl.startsWith('@') && !packageUrl.startsWith('~')) {
    return null;
  }

  // Strip a leading `~` (legacy webpack-style package prefix).
  const pkg = packageUrl.startsWith('~') ? packageUrl.slice(1) : packageUrl;

  // pnpm's flat layout + Nx workspace can place the package either
  // at the workspace root node_modules OR inside a per-package
  // node_modules. Probe both.
  const searchDirs = [
    NODE_MODULES,
    path.join(__dirname, 'node_modules'),
    path.resolve(__dirname, '..', 'node_modules'),
  ];

  const entryCandidates = [
    '_index.scss',
    'index.scss',
    '_typography.scss',
    '_definition.scss',
    '_all.scss',
    '_index.css',
  ];

  for (const dir of searchDirs) {
    const pkgDir = path.join(dir, pkg);
    if (!fs.existsSync(pkgDir)) continue;
    for (const entry of entryCandidates) {
      const full = path.join(pkgDir, entry);
      if (fs.existsSync(full)) {
        return new URL('file:///' + full.replace(/\\/g, '/'));
      }
    }
  }

  return null;
}

module.exports = (config) => {
  if (!config || !config.module || !Array.isArray(config.module.rules)) {
    return config;
  }

  for (const rule of config.module.rules) {
    if (!rule || !rule.test) continue;
    if (!rule.test.toString().includes('scss')) continue;

    const uses = Array.isArray(rule.use)
      ? rule.use
      : rule.use
        ? [rule.use]
        : [];

    for (const useEntry of uses) {
      if (!useEntry || typeof useEntry !== 'object') continue;
      const loader = useEntry.loader || '';
      if (typeof loader !== 'string' || !loader.includes('sass-loader')) {
        continue;
      }

      useEntry.options = useEntry.options || {};
      useEntry.options.sassOptions = useEntry.options.sassOptions || {};
      const opts = useEntry.options.sassOptions;
      opts.importers = Array.isArray(opts.importers) ? opts.importers : [];

      // Only push our importer once per rule.
      if (!opts.importers.some((i) => i && i.__avgcxrPkg)) {
        const importer = {
          __avgcxrPkg: true,
          findFileUrl(url) {
            return resolvePackageToScss(url);
          },
        };
        opts.importers.push(importer);
      }

      // Also add node_modules to loadPaths as a belt-and-suspenders
      // fallback for any file: URL that points to a directory.
      opts.loadPaths = Array.isArray(opts.loadPaths) ? opts.loadPaths : [];
      const want = NODE_MODULES;
      if (!opts.loadPaths.includes(want)) opts.loadPaths.push(want);

      break;
    }
  }

  return config;
};
