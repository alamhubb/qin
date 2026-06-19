# OVS + CSSTS Qin/Vite Demos

These demos compare the same OVS/CSSTS UI source with different Slime parser routes.

## Layout

- `qin-ovs-cssts-java-slime-demo`: Qin dev server, TypeScript OVS/CSSTS parser bridge compiled to JVM classes and extending Java SlimeParser.
- `qin-ovs-cssts-ts-slime-demo`: Qin dev server, handwritten TypeScript Slime/OVS/CSSTS route.
- `qin-ovs-cssts-generated-ts-slime-demo`: Qin dev server, Java SlimeParser generated to TypeScript and loaded as a local package.
- `vite-ovs-cssts-generated-ts-slime-demo`: Vite comparison using the committed generated TypeScript Slime parser package.

## Workspace

Clone these repositories as siblings under one workspace directory:

- `qin`
- `slime`
- `subhuti`
- `ovsjs`
- `cssts`
- `glogjs`
- `java-sdk`

The Qin demos use `qin.config.js` and local package overrides pointing from this directory back to those sibling repositories.

## Run

From `qin`:

```powershell
.\qin.bat sync
```

Generated TypeScript Slime Qin demo:

```powershell
cd examples\ovs-cssts-demos\qin-ovs-cssts-generated-ts-slime-demo
..\..\..\qin.bat sync
..\..\..\qin.bat dev
```

Open `http://127.0.0.1:19115/`.

Vite comparison:

```powershell
cd examples\ovs-cssts-demos\vite-ovs-cssts-generated-ts-slime-demo
npm install
npm run dev
```

Open `http://127.0.0.1:19113/`.
