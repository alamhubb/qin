# Qin User DB Fullstack Demo

Single-port Qin fullstack demo:

- frontend: Vue SFC + OVS + CSSTS served by Qin
- backend: Qin-owned `QinHttpApp` routes served by Qin dev server
- database: remote PostgreSQL through JDBC

Run:

```powershell
$env:QIN_DEMO_DB_PASSWORD = "<password>"
..\..\qin.bat sync
..\..\qin.bat dev
```

Optional database overrides:

```powershell
$env:QIN_DEMO_DB_URL = "jdbc:postgresql://43.143.220.49:5432/qin_demo"
$env:QIN_DEMO_DB_USER = "qin_user"
```

Open:

```text
http://127.0.0.1:19116/
```
