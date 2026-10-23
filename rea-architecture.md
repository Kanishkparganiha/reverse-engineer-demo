# REA — High-Level Architecture

## 1. Layers

```mermaid
flowchart TB
    subgraph USERS["Who asks"]
        HUMAN["👤 Analyst terminal"]
        AGENT["🤖 AI agent Claude Code · Qwen Code · LangGraph"]
    end

    subgraph ENTRY["Entry points"]
        CLI["CLI  rea &lt;command&gt;</code> one-shot"]
        MCP["MCP server  rea mcp</code> long-lived sessions"]
    end

    CONTRACTS["Tool contracts one definition per tool: inputs · outputs · side effects"]

    subgraph APP["Shared workflows"]
        ROUTER["Provider selection deterministic, no silent fallback"]
        SESSION["Sessions & snapshots reuse opened targets"]
        LEDGER["Evidence & unknowns provenance ledger"]
    end

    subgraph ENGINES["Analysis engines (providers)"]
        BUILTIN["Built-in SQLite · plist · Mach-O WASM · JavaScript · pcap · .NET"]
        BYO["Bring your own Ghidra · IDA · Hopper · rizin JADX · JEB · Frida · gdb · adb"]
    end

    TARGET[("Target artifact binary · app · package · database")]

    EVIDENCE[/"📄 Evidence record SHA-256 · provider version · parameters results · limitations observed / inferred / unknown"/]

    HUMAN --> CLI
    AGENT --> MCP
    CLI --> CONTRACTS
    MCP --> CONTRACTS
    CONTRACTS --> APP
    ROUTER --> ENGINES
    SESSION --> ENGINES
    ENGINES -->|"read-only copy bounded worker"| TARGET
    ENGINES --> LEDGER
    LEDGER --> EVIDENCE
    EVIDENCE -.-> HUMAN
    EVIDENCE -.-> AGENT
```

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
