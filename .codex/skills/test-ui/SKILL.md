---
name: test-ui
description: Run planned console UI tests from test/ui-test-plan.md, compare each session's output exactly, and stop at the first failure.
---

# Console UI testing

Use this skill when testing the project's command-line user interface against planned input/output sessions.

## Test plan

Keep the test cases in `test/ui-test-plan.md`. Each case must have an aim, an `Inputs` fenced `text` block, and an `Expected output` fenced `text` block. The plan's `test-ui-run` comment supplies the command that launches the application. Inputs are supplied to one fresh program session in their listed order.

Add or revise cases before running them when the requested behavior changes. Expected output must be the complete console output, including banners, prompts, dividers, and blank lines. Do not weaken comparisons with partial matching unless the user explicitly requests it.

## Run the tests

1. Compile the project using Java 25, if the plan's run command needs compilation.
2. Run `scripts/run-ui-tests.ps1` from the repository root. Pass `-Plan` if the plan is not at its default location.
3. Present the generated `test/ui-test-session.md` in the response. It records every test session's console input and output.

The runner compares normalized line endings but otherwise compares output exactly. It creates a new process for each case. On a failure, it immediately stops, writes the transcript with the expected and actual output, and exits unsuccessfully. Report both outputs and do not run later cases.

## Plan format

Use the structure in the existing plan. The run command is declared once, for example:

`<!-- test-ui-run: java -cp build/classes Edith -->`

Use Markdown headings beginning with `## Test case` so the runner can identify cases.
