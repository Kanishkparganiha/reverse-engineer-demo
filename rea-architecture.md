# REA — High-Level Architecture

## 1. Layers

!(./rea_demo/HighLevelDiagram.png)

## 2. One request, end to end

```mermaid
sequenceDiagram
    autonumber
    participant U as Analyst / AI agent
    participant R as REA (CLI or MCP)
    participant W as Shared workflow
    participant E as Engine (e.g. Ghidra, SQLite)
    participant T as Target file

    U->>R: Ask a question (tool + path)
    R->>R: Validate input against tool contract
    R->>W: Run workflow
    W->>T: Hash + copy to private snapshot
    W->>E: Start supervised, resource-limited worker
    E->>T: Analyze the copy (original untouched)
    E-->>W: Raw engine output
    W->>W: Normalize + label observed / inferred / unknown
    W-->>R: Evidence record
    R-->>U: Answer with provenance and limitations
```

## Key messages

- **Two front doors, one engine room** — CLI and MCP share the same workflows, so results match.
- **REA orchestrates, engines analyze** — Ghidra, JADX, Frida, etc. do the heavy lifting.
- **Everything returns Evidence** — hashes, engine version, limitations, and what is *known* vs *guessed*.
- **Safe by default** — works on copies, bounded workers, target content treated as untrusted.
