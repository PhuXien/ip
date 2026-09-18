# Edith User Guide

Edith helps you keep a simple task list through a chat window. Type a command in the input box and press **Enter** or click **Send**. Your tasks are saved automatically and restored the next time you start Edith.

![Edith chat window showing a task list and example commands](Ui.png.png)

## Start Edith

Install **JDK 25**, open this project in IntelliJ, and run `gradlew.bat run` in its terminal (Windows). On macOS or Linux, run `./gradlew run`. You can also run the `edith.gui.Launcher` class in IntelliJ. See the [project README](../README.md) for IntelliJ setup.

## Commands at a glance

Replace words in CAPITALS with your own text. Square brackets show optional parts; do not type the brackets.

| What you want to do | Command | Example |
| --- | --- | --- |
| Add a task | `todo DESCRIPTION [/tags #TAG...]` | `todo read book /tags #fun` |
| Add a task with a due date | `deadline DESCRIPTION /by yyyy-MM-dd [HHmm] [/tags #TAG...]` | `deadline submit report /by 2026-09-20 1800 /tags #school` |
| Add an event | `event DESCRIPTION /from yyyy-MM-dd [HHmm] /to yyyy-MM-dd [HHmm] [/tags #TAG...]` | `event workshop /from 2026-09-21 0900 /to 2026-09-21 1100` |
| See all tasks | `list` | `list` |
| Search tasks | `find KEYWORD` | `find report` or `find #school` |
| Complete or reopen a task | `mark NUMBER` / `unmark NUMBER` | `mark 2` |
| Remove a task | `delete NUMBER` | `delete 2` |
| Add or remove tags | `tag NUMBER #TAG...` / `untag NUMBER #TAG...` | `tag 2 #urgent #school` |
| End the chat | `bye` | `bye` |

Dates use `yyyy-MM-dd` (for example, `2026-09-20`); optional times use 24-hour `HHmm` (for example, `1800`). An event must end after it starts. Edith shows task types as `[T]` (todo), `[D]` (deadline), or `[E]` (event), followed by `[ ]` for unfinished or `[X]` for finished.

Use the number shown by **`list`** for `mark`, `unmark`, `delete`, `tag`, and `untag`. `find` numbers its results separately, so run `list` before changing a task you found.

Tags start with `#` and contain letters, digits, `_`, or `-`. Put `/tags` and at least one tag **at the end** of a new task command. For an existing task, use `tag` or `untag`; no `/tags` is needed. Tag names ignore case, so `#School` and `#school` refer to the same tag.

`find WORD` searches descriptions and tags without regard to case. `find #school` searches for that exact tag; it will not match `#schoolwork` or a description that merely mentions `#school`. Edith keeps tasks in their original order in search results.

Your tasks are stored in `data/edith.txt` relative to the directory from which you start Edith. If Edith reports that it cannot load this file, fix the file before entering more commands; Edith will not overwrite it.
