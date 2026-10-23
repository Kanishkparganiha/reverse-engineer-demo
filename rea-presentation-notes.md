# REA —  Summary 


**1. The problem**
> "Sometimes all you have is the compiled artifact — a binary, an app package, a database — and
> no source or documentation. Reverse-engineering tools exist, but each has its own interface,
> and AI agents can't use them safely or consistently."

**2. What REA is**
> "REA puts one interface in front of many analysis engines. You can use it from the terminal as
> a CLI, or let an AI agent drive it through MCP — the Model Context Protocol. Both paths run
> the exact same workflows, so the results match."

**3. How it works** *(show Layers diagram)*
> "A request comes in through the CLI or MCP and is validated against a tool contract. A shared
> workflow picks the right engine — deterministically, with no silent fallback — and runs it on a
> private, read-only copy of the target inside a resource-limited worker. The raw output is
> normalized into Evidence."

**4. Engines** *(point at the engines box)*
> "Simple formats — SQLite, plists, WebAssembly, JavaScript bundles, .NET metadata — are handled
> by built-in inspectors. Deep analysis uses engines you bring yourself: Ghidra or IDA for native
> code, JADX for Android, Frida and gdb for runtime behavior."

**5. Evidence is the product** *(show sequence diagram, steps 4–10)*
> "Every result carries the file's SHA-256, the engine and version, the exact parameters, and an
> explicit list of limitations. Findings are labeled as observed, derived, inferred or unknown —
> so an AI agent can't quietly turn a guess into a fact."

**6. Safe by default**
> "REA works on copies, never modifies the target, bounds memory and time, and treats anything
> read from the target — strings, decompiled code, page text — as untrusted data, which guards
> against prompt injection when an agent is driving."

**7. Honest limits**
> "REA is only as capable as the engines behind it. Native decompilation needs Ghidra or IDA
> installed. And some formats aren't routed yet — for example, plain Java/Scala JARs: today
> REA's JADX integration targets Android APKs. Reverse engineering recovers *logic*, never data
> that a program already threw away."

**8. Close**
> "So: one interface, many engines, for humans and AI agents alike — and every answer comes
> with its receipts."


## Read more

**REA**
- Repository & README — https://github.com/morluto/rea
- How REA works (README section) — https://github.com/morluto/rea#how-rea-works
- What REA can analyze — https://github.com/morluto/rea#what-you-can-analyze
- Choosing a deep-analysis provider — https://github.com/morluto/rea#choosing-a-deep-analysis-provider
- Installation & setup — https://github.com/morluto/rea/blob/main/docs/installation.md
- CLI and Evidence — https://github.com/morluto/rea/blob/main/docs/cli.md
- Agent / client configuration — https://github.com/morluto/rea/blob/main/docs/client-configuration.md
- Tool design principles — https://github.com/morluto/rea/blob/main/docs/tool-design.md
- Full architecture diagram — https://github.com/morluto/rea/blob/main/docs/architecture.mermaid
- Android (JADX) analysis — https://github.com/morluto/rea/blob/main/docs/android-analysis.md
- .NET managed-code analysis — https://github.com/morluto/rea/blob/main/docs/managed-code-analysis.md
- npm package — https://www.npmjs.com/package/rea-agents

**Background**
- Model Context Protocol — https://modelcontextprotocol.io
- Ghidra (NSA, free) — https://ghidra-sre.org
- JADX (Java/Android decompiler) — https://github.com/skylot/jadx
- Frida (runtime instrumentation) — https://frida.re
- Ollama (local models) — https://ollama.com
- LangChain MCP adapters — https://github.com/langchain-ai/langchain-mcp-adapters
