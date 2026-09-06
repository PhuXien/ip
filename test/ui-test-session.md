# UI Test Session

Ran with Java 25.0.4.1 in a clean temporary working directory. All actual console outputs were captured and compared exactly (after line-ending normalization) with the corresponding expected output in `ui-test-plan.md`.

## Test case 1

**Inputs:** `todo read book`, `deadline return book /by 2019-12-02 1800`, `event project meeting /from 2019-08-06 1400 /to 2019-08-06 1600`, task updates and listing, then `bye`.

**Actual output:** Exact match with the planned output.

## Test case 2

**Inputs:** Invalid todo, deadline, event, mark, delete, and unknown-command inputs, then `bye`.

**Actual output:** Exact match with the planned output.

## Test case 3

**Inputs:** `list`, then `bye`, continuing from the saved tasks in the prior sessions.

**Actual output:** Exact match with the planned output.

## Test case 4

**Inputs:** `unmark 1`, `list`, then `bye`.

**Actual output:** Exact match with the planned output.
