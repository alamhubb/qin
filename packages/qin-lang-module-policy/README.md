# qin-lang-module-policy

Zone-based module import policy layer for Qin.

## Responsibility

- Parse import declarations into neutral descriptors.
- Parse `export ... from` declarations into the same descriptor stream.
- Classify import kinds (`java`, `js`, `qin local`, unknown).
- Detect source zone by project layout (`app`, `main`, `shared`).
- Enforce compile-time rules and return structured violations.

## Rule Set

- `app/` (frontend): allow JS, deny `java:` (`QIN1001`)
- `main/` (backend): allow `java:` and JS imports
- `shared/`: deny `java:` and JS (`QIN1003`)
