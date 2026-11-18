# Architecture Decision Records (ADRs)

An ADR is a short document that records **one important technical decision**: the context,
the options considered, the decision and its consequences.

Why keep them: months later nobody remembers *why* a choice was made. ADRs make the reasoning
reviewable, and they are strong interview material.

## Convention

- One file per decision, numbered: `0001-modular-monolith-first.md`, `0002-use-flyway.md`, ...
- Sections: **Status** (proposed / accepted / superseded), **Context**, **Decision**, **Consequences**.
- Never rewrite history: if a decision changes, add a new ADR and mark the old one superseded.

The first ADR (modular monolith first) is planned for Task 0.8.
