const fs = require('fs');
const path = require('path');

const distDir = path.join(__dirname, '..', 'dist', 'app', 'browser');
const enDir = path.join(distDir, 'en');

if (!fs.existsSync(enDir)) {
  console.error('Error: en/ directory not found at', enDir);
  process.exit(1);
}

function copyRecursive(src, dest) {
  const stat = fs.statSync(src);
  if (stat.isDirectory()) {
    fs.mkdirSync(dest, { recursive: true });
    for (const entry of fs.readdirSync(src)) {
      copyRecursive(path.join(src, entry), path.join(dest, entry));
    }
  } else {
    fs.copyFileSync(src, dest);
  }
}

// Copy en/ contents to the root dist directory
for (const entry of fs.readdirSync(enDir)) {
  const srcPath = path.join(enDir, entry);
  const destPath = path.join(distDir, entry);

  if (entry === 'en' || entry === 'de') continue;

  copyRecursive(srcPath, destPath);
}

// Rewrite <base href="/en/"> to <base href="/"> in all root HTML files
function fixBaseHref(dir) {
  for (const entry of fs.readdirSync(dir)) {
    const fullPath = path.join(dir, entry);
    // Skip locale directories
    if (dir === distDir && (entry === 'en' || entry === 'de')) continue;

    const stat = fs.statSync(fullPath);
    if (stat.isDirectory()) {
      fixBaseHref(fullPath);
    } else if (entry.endsWith('.html')) {
      let content = fs.readFileSync(fullPath, 'utf8');
      if (content.includes('<base href="/en/">')) {
        content = content.replace('<base href="/en/">', '<base href="/">');
        fs.writeFileSync(fullPath, content);
      }
    }
  }
}

fixBaseHref(distDir);

console.log('Post-build: copied en/ contents to root and fixed base href');
