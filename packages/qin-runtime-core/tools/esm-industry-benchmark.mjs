import fs from "node:fs";
import path from "node:path";
import { createRequire } from "node:module";
import { performance } from "node:perf_hooks";

const [modulesFile] = process.argv.slice(2);
if (!modulesFile) {
  throw new Error("Usage: node esm-industry-benchmark.mjs <modules.txt>");
}

const requireFromCwd = createRequire(path.join(process.cwd(), "package.json"));
const modulePaths = fs.readFileSync(modulesFile, "utf8")
  .split(/\r?\n/)
  .map((line) => line.trim())
  .filter(Boolean);
const modules = modulePaths.map((file) => ({
  file,
  source: fs.readFileSync(file, "utf8")
}));
const chars = modules.reduce((sum, item) => sum + item.source.length, 0);

console.log(`benchmark=industry-tools stage=input modules=${modules.length} chars=${chars}`);

await benchmark("typescript-createSourceFile", () => {
  const ts = requireFromCwd("typescript");
  let imports = 0;
  let exports = 0;
  for (const item of modules) {
    const sourceFile = ts.createSourceFile(
      item.file,
      item.source,
      ts.ScriptTarget.Latest,
      false,
      scriptKind(ts, item.file)
    );
    for (const statement of sourceFile.statements) {
      if (ts.isImportDeclaration(statement)) {
        imports++;
      } else if (isTsExport(statement, ts)) {
        exports++;
      }
    }
  }
  return { imports, exports };
});

await benchmark("babel-parser", () => {
  const parser = requireFromCwd("@babel/parser");
  let imports = 0;
  let exports = 0;
  for (const item of modules) {
    const ast = parser.parse(item.source, {
      sourceType: "module",
      plugins: [
        "typescript",
        "decorators-legacy",
        "classProperties",
        "classPrivateProperties",
        "classPrivateMethods",
        "exportDefaultFrom",
        "exportNamespaceFrom",
        "importMeta",
        "topLevelAwait"
      ]
    });
    for (const statement of ast.program.body) {
      if (statement.type === "ImportDeclaration") {
        imports++;
      } else if (statement.type.startsWith("Export")) {
        exports++;
      }
    }
  }
  return { imports, exports };
});

await benchmark("esbuild-transform", async () => {
  const esbuild = requireFromCwd("esbuild");
  let outputs = 0;
  for (const item of modules) {
    await esbuild.transform(item.source, {
      loader: loaderFor(item.file),
      format: "esm",
      target: "es2022",
      logLevel: "silent"
    });
    outputs++;
  }
  return { outputs };
});

await benchmark("swc-parse", () => {
  const swc = requireFromCwd("@swc/core");
  let imports = 0;
  let exports = 0;
  for (const item of modules) {
    const ast = swc.parseSync(item.source, {
      syntax: item.file.endsWith(".ts") || item.file.endsWith(".tsx") ? "typescript" : "ecmascript",
      tsx: item.file.endsWith(".tsx"),
      decorators: true,
      target: "es2022"
    });
    for (const statement of ast.body || []) {
      if (statement.type === "ImportDeclaration") {
        imports++;
      } else if (statement.type.startsWith("Export")) {
        exports++;
      }
    }
  }
  return { imports, exports };
});

await benchmark("oxc-parser", () => {
  const oxc = requireFromCwd("oxc-parser");
  let imports = 0;
  let exports = 0;
  for (const item of modules) {
    const ast = oxc.parseSync(item.file, item.source, { sourceType: "module" });
    const body = ast.program?.body || ast.body || [];
    for (const statement of body) {
      if (statement.type === "ImportDeclaration") {
        imports++;
      } else if (statement.type?.startsWith("Export")) {
        exports++;
      }
    }
  }
  return { imports, exports };
});

async function benchmark(name, fn) {
  const started = performance.now();
  try {
    const result = await fn();
    const elapsed = performance.now() - started;
    console.log(`benchmark=industry-tools tool=${name} status=ok elapsedMs=${elapsed.toFixed(3)} ${fields(result)}`);
  } catch (error) {
    const elapsed = performance.now() - started;
    console.log(`benchmark=industry-tools tool=${name} status=failed elapsedMs=${elapsed.toFixed(3)} detail=${quoteDetail(error?.message || String(error))}`);
  }
}

function scriptKind(ts, file) {
  if (file.endsWith(".tsx")) return ts.ScriptKind.TSX;
  if (file.endsWith(".jsx")) return ts.ScriptKind.JSX;
  if (file.endsWith(".js") || file.endsWith(".mjs") || file.endsWith(".cjs")) return ts.ScriptKind.JS;
  return ts.ScriptKind.TS;
}

function loaderFor(file) {
  if (file.endsWith(".tsx")) return "tsx";
  if (file.endsWith(".jsx")) return "jsx";
  if (file.endsWith(".js") || file.endsWith(".mjs") || file.endsWith(".cjs")) return "js";
  return "ts";
}

function isTsExport(statement, ts) {
  return ts.isExportDeclaration(statement)
    || ts.isExportAssignment(statement)
    || Boolean(statement.modifiers?.some((modifier) => modifier.kind === ts.SyntaxKind.ExportKeyword));
}

function fields(result) {
  return Object.entries(result || {})
    .map(([key, value]) => `${key}=${value}`)
    .join(" ");
}

function quoteDetail(text) {
  const compact = String(text).replace(/\s+/g, " ").slice(0, 240);
  return JSON.stringify(compact);
}
