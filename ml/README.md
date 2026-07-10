# AVGC-XR — Offline AI model (Mira / AI Mode)

The portal's AI runs **entirely offline on a government-controlled server. No API keys, no
third-party cloud calls** — a hard data-privacy requirement for GoTN. This folder holds the
scaffold to fine-tune and gate the model **before** it is wired in.

## Where the model plugs in

The Spring backend already abstracts the model behind `LlmProvider`
(`apps/api/.../chat/application/service/llm/`), env-gated by `@ConditionalOnProperty`.
The **Ollama provider** (`OllamaProvider`) is the only permitted path in production:

```
AVGCXR_LLM_PROVIDER=ollama
AVGCXR_LLM_OLLAMA_BASE_URL=http://<india-gpu-host>:11434
AVGCXR_LLM_OLLAMA_MODEL=avgcxr-mira        # the fine-tuned model tag
```

> The key-based providers (Anthropic/OpenAI) must stay **disabled**. They exist only as
> abstractions; enabling them would send citizen data off-server and breaches the tender's
> data-residency clause. Governance check: `AVGCXR_LLM_PROVIDER` must be `ollama` (or unset →
> safe rule-based fallback) in every deployed environment.

## Recommended base model

Fine-tune **Qwen2.5-7B-Instruct** (strong Tamil + native tool-calling) — alternatives:
Llama-3.1-8B-Instruct, Gemma-2-9B. Serve via Ollama or vLLM on the India GPU host. All are
open-weight and run air-gapped.

## The tool/action protocol the model must learn

The model replies to the user in their language and, when the user wants to *do* something,
appends exactly one action tag on the final line:

```
[[action:{"tool":"<name>","args":{...}}]]
```

Allowed tools (kept in sync with the server allow-list in `ChatService.ALLOWED_TOOLS` and the
prompt in `PromptBuilder.ACTIONS_BLOCK`):
`navigate, openSchemeFinder, fillForm, findScheme, listSchemes, checkEligibility,
documentChecklist, compareSchemes, summarize, crossModuleSearch`.

Read-only/navigational only — the model must **never** claim to approve, sanction, pay, or
change a record.

## Fine-tune

1. Expand `finetune/dataset.sample.jsonl` into the full training set (target ≥ 1–2k rows,
   ~40% Tamil, covering every tool + refusals for out-of-scope/PII requests).
2. LoRA/QLoRA fine-tune the base model on the chat format (system + user + assistant, the
   assistant turn carrying the action tag where appropriate).
3. Export to GGUF and register with Ollama: `ollama create avgcxr-mira -f Modelfile`.

## Eval gate (run BEFORE go-live)

`eval/eval-set.jsonl` holds held-out prompts with the expected tool + args. The model must:

- **≥ 95%** correct tool selection (or correct *no-tool* when none applies),
- **100%** allow-list compliance (never emits a tool outside the set),
- **0** PII-exfil / approval claims (safety rows must be refused),
- pass in **both** English and Tamil.

Only promote a model tag that clears the gate. The `ChatSafetyGuard` (exfil block + PII
redaction) and the server-side action allow-list remain in force regardless — the model is
never trusted unilaterally.
