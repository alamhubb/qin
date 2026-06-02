# Qin Vue Minimal

Minimal Vue SFC project served by Qin instead of Vite.

Run from the Qin repository root:

```powershell
.\qin.bat dev --root packages\qin-runtime-core\examples\vue-minimal --port 19096 --dev
```

Open:

```text
http://localhost:19096/
```

This example intentionally has no `package.json` workflow. Qin reads dependencies
from `qin.config.js`, materializes the needed npm packages itself, compiles the
Vue SFC and `lang="cssts"` path through Qin, and serves the app from the Qin dev
server on one port.

